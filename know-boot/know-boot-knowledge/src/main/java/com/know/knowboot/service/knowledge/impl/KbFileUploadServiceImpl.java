package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbDocument;
import com.know.knowboot.entity.knowledge.KbKnowledgeBase;
import com.know.knowboot.entity.knowledge.KbUploadedFile;
import com.know.knowboot.mapper.knowledge.KbDocumentMapper;
import com.know.knowboot.mapper.knowledge.KbKnowledgeBaseMapper;
import com.know.knowboot.mapper.knowledge.KbUploadedFileMapper;
import com.know.knowboot.service.knowledge.IKbFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件上传服务实现
 */
@Service
public class KbFileUploadServiceImpl extends ServiceImpl<KbUploadedFileMapper, KbUploadedFile> implements IKbFileUploadService {

    @Autowired
    private KbUploadedFileMapper kbUploadedFileMapper;

    @Autowired
    private KbDocumentMapper kbDocumentMapper;

    @Autowired
    private KbKnowledgeBaseMapper kbKnowledgeBaseMapper;

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    private static final Set<String> ALLOWED_EXTS = new HashSet<>(Arrays.asList("html", "htm", "md", "markdown"));
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchUpload(Object[] files, Long knowledgeBaseId, String uploadPlatform, Long userId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> items = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        for (Object f : files) {
            MultipartFile file = (MultipartFile) f;
            Map<String, Object> item = new HashMap<>();
            item.put("originalName", file.getOriginalFilename());

            try {
                String originalName = file.getOriginalFilename();
                String ext = getExt(originalName).toLowerCase();
                if (!ALLOWED_EXTS.contains(ext)) {
                    item.put("parseStatus", 2);
                    item.put("parseError", "不支持的文件类型: ." + ext);
                    failCount++;
                    items.add(item);
                    continue;
                }

                if (file.getSize() > MAX_FILE_SIZE) {
                    item.put("parseStatus", 2);
                    item.put("parseError", "文件超过5MB限制");
                    failCount++;
                    items.add(item);
                    continue;
                }

                String datePath = new SimpleDateFormat("yyyyMMdd").format(new Date());
                String fileName = UUID.randomUUID().toString().replace("-", "") + "." + ext;
                String relativePath = "knowledge/" + datePath + "/" + fileName;
                String fullPath = uploadPath + "/" + relativePath;

                File dir = new File(uploadPath + "/knowledge/" + datePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                byte[] bytes = file.getBytes();
                Files.write(Paths.get(fullPath), bytes);

                String content = new String(bytes, StandardCharsets.UTF_8);
                if (content.startsWith("\uFEFF")) {
                    content = content.substring(1);
                }

                KbUploadedFile entity = new KbUploadedFile();
                entity.setOriginalName(originalName);
                entity.setFileName(fileName);
                entity.setFilePath(relativePath);
                entity.setFileExt(ext);
                entity.setFileContent(content);
                entity.setFileSize(file.getSize());
                entity.setMimeType(file.getContentType());
                entity.setKnowledgeBaseId(knowledgeBaseId);
                entity.setParseStatus(0);
                entity.setSourceType("upload");
                entity.setUploadPlatform(uploadPlatform);
                entity.setSort(0);
                entity.setStatus(1);
                entity.setCreateBy(userId);
                entity.setCreateTime(System.currentTimeMillis());
                save(entity);

                try {
                    parse(entity.getId());
                    KbUploadedFile updated = getById(entity.getId());
                    item.put("parseStatus", updated.getParseStatus());
                    item.put("documentId", updated.getDocumentId());
                    successCount++;
                } catch (Exception e) {
                    KbUploadedFile failEntity = getById(entity.getId());
                    if (failEntity != null) {
                        failEntity.setParseStatus(2);
                        failEntity.setParseError(e.getMessage());
                        updateById(failEntity);
                    }
                    item.put("parseStatus", 2);
                    item.put("parseError", e.getMessage());
                    failCount++;
                }
            } catch (IOException e) {
                item.put("parseStatus", 2);
                item.put("parseError", "IO异常: " + e.getMessage());
                failCount++;
            }
            items.add(item);
        }

        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("items", items);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean parse(Long fileId) {
        KbUploadedFile file = getById(fileId);
        if (file == null) {
            throw new RuntimeException("文件不存在");
        }

        if (file.getParseStatus() != null && file.getParseStatus() == 1 && file.getDocumentId() != null) {
            kbDocumentMapper.deleteById(file.getDocumentId());
        }

        String ext = file.getFileExt().toLowerCase();
        // content_type 在 kb_document 中为 tinyint: 1=富文本(HTML), 2=Markdown
        String contentType;
        if ("html".equals(ext) || "htm".equals(ext)) {
            contentType = "1";
        } else if ("md".equals(ext) || "markdown".equals(ext)) {
            contentType = "2";
        } else {
            throw new RuntimeException("不支持的扩展名: " + ext);
        }

        String title = file.getOriginalName();
        if (title.contains(".")) {
            title = title.substring(0, title.lastIndexOf("."));
        }

        // knowledge_base_id 在 kb_document 中为 NOT NULL, 若未指定则取用户的第一个知识库
        Long kbId = file.getKnowledgeBaseId();
        if (kbId == null) {
            KbKnowledgeBase firstKb = kbKnowledgeBaseMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<KbKnowledgeBase>()
                            .eq(KbKnowledgeBase::getCreateBy, file.getCreateBy())
                            .orderByAsc(KbKnowledgeBase::getId)
                            .last("LIMIT 1"));
            if (firstKb == null) {
                throw new RuntimeException("请先创建知识库, 再上传文件");
            }
            kbId = firstKb.getId();
        }

        KbDocument doc = new KbDocument();
        doc.setTitle(title);
        doc.setContent(file.getFileContent());
        doc.setContentType(contentType);
        doc.setKnowledgeBaseId(kbId);
        doc.setViewCount(0);
        doc.setLikeCount(0);
        doc.setFavoriteCount(0);
        doc.setCommentCount(0);
        doc.setVersion(1);
        doc.setSort(0);
        doc.setStatus(1);
        doc.setCreateBy(file.getCreateBy());
        doc.setCreateTime(System.currentTimeMillis());
        kbDocumentMapper.insert(doc);

        file.setDocumentId(doc.getId());
        file.setParseStatus(1);
        file.setParseError(null);
        file.setUpdateTime(System.currentTimeMillis());
        updateById(file);

        if (file.getKnowledgeBaseId() != null) {
            KbKnowledgeBase kb = kbKnowledgeBaseMapper.selectById(file.getKnowledgeBaseId());
            if (kb != null) {
                kb.setDocCount((kb.getDocCount() == null ? 0 : kb.getDocCount()) + 1);
                kb.setUpdateTime(System.currentTimeMillis());
                kbKnowledgeBaseMapper.updateById(kb);
            }
        }

        return true;
    }

    @Override
    public IPage<KbUploadedFile> page(KbUploadedFile query, Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<KbUploadedFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getKnowledgeBaseId() != null, KbUploadedFile::getKnowledgeBaseId, query.getKnowledgeBaseId())
                .eq(query.getParseStatus() != null, KbUploadedFile::getParseStatus, query.getParseStatus())
                .eq(query.getCreateBy() != null, KbUploadedFile::getCreateBy, query.getCreateBy())
                .like(query.getOriginalName() != null, KbUploadedFile::getOriginalName, query.getOriginalName())
                .orderByDesc(KbUploadedFile::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeWithDocument(Long id) {
        KbUploadedFile file = getById(id);
        if (file == null) {
            return true;
        }
        if (file.getDocumentId() != null) {
            kbDocumentMapper.deleteById(file.getDocumentId());
            if (file.getKnowledgeBaseId() != null) {
                KbKnowledgeBase kb = kbKnowledgeBaseMapper.selectById(file.getKnowledgeBaseId());
                if (kb != null && kb.getDocCount() != null && kb.getDocCount() > 0) {
                    kb.setDocCount(kb.getDocCount() - 1);
                    kb.setUpdateTime(System.currentTimeMillis());
                    kbKnowledgeBaseMapper.updateById(kb);
                }
            }
        }
        return removeById(id);
    }

    private String getExt(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
