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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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

    /** 允许上传的扩展名(不含点, 小写) */
    private static final Set<String> ALLOWED_EXTS = new HashSet<>(Arrays.asList("html", "htm", "md", "markdown", "pdf"));
    /** 文本类扩展名: 需要读取为 UTF-8 文本内容并写入 kb_uploaded_file.file_content */
    private static final Set<String> TEXT_EXTS = new HashSet<>(Arrays.asList("html", "htm", "md", "markdown"));
    /** 文本类文件大小上限: 5MB */
    private static final long MAX_TEXT_FILE_SIZE = 5 * 1024 * 1024;
    /** PDF 文件大小上限: 10MB */
    private static final long MAX_PDF_FILE_SIZE = 10 * 1024 * 1024;
    /** PDF 预览图片缓存目录（位于 uploadPath 下，经 nginx /uploads 静态公开访问） */
    private static final String PDF_PREVIEW_DIR = "pdf-preview";
    /** 单次最多渲染页数，避免超大 PDF 拖垮服务 */
    private static final int PDF_PREVIEW_MAX_PAGES = 50;
    /** PDF 渲染 DPI（兼顾清晰度与体积） */
    private static final float PDF_PREVIEW_DPI = 120f;

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

                long maxSize = isPdf(ext) ? MAX_PDF_FILE_SIZE : MAX_TEXT_FILE_SIZE;
                if (file.getSize() > maxSize) {
                    item.put("parseStatus", 2);
                    item.put("parseError", "文件超过" + (maxSize / 1024 / 1024) + "MB限制");
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

                // 仅文本类文件读取为 UTF-8 文本; PDF 等二进制文件不写入 file_content
                String content = null;
                if (TEXT_EXTS.contains(ext)) {
                    content = new String(bytes, StandardCharsets.UTF_8);
                    if (content.startsWith("\uFEFF")) {
                        content = content.substring(1);
                    }
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
        // content_type 在 kb_document 中为 tinyint: 1=富文本(HTML), 2=Markdown, 3=PDF
        String contentType;
        boolean pdf = isPdf(ext);
        if ("html".equals(ext) || "htm".equals(ext)) {
            contentType = "1";
        } else if ("md".equals(ext) || "markdown".equals(ext)) {
            contentType = "2";
        } else if (pdf) {
            contentType = "3";
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
        // PDF 不写入文本内容, 预览时走原始文件渲染
        doc.setContent(pdf ? null : file.getFileContent());
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

    @Override
    public synchronized Map<String, Object> pdfPages(Long fileId) {
        KbUploadedFile file = getById(fileId);
        if (file == null) {
            throw new RuntimeException("文件不存在");
        }
        String ext = file.getFileExt() == null ? "" : file.getFileExt().toLowerCase();
        if (!isPdf(ext)) {
            throw new RuntimeException("不是 PDF 文件");
        }
        String rel = file.getFilePath();
        if (rel == null || rel.contains("..")) {
            throw new RuntimeException("文件路径非法");
        }
        File src = Paths.get(uploadPath, rel).normalize().toAbsolutePath().toFile();
        if (!src.exists() || !src.isFile()) {
            throw new RuntimeException("源文件不存在");
        }

        File dir = new File(uploadPath, PDF_PREVIEW_DIR + "/" + fileId);
        File meta = new File(dir, "pages.txt");

        Map<String, Object> result = new HashMap<>();
        List<String> urls = new ArrayList<>();

        // 命中缓存
        if (dir.exists() && meta.exists()) {
            try {
                int n = Integer.parseInt(new String(Files.readAllBytes(meta.toPath()), StandardCharsets.UTF_8).trim());
                if (n > 0) {
                    for (int i = 1; i <= n; i++) {
                        if (new File(dir, "page-" + i + ".jpg").exists()) {
                            urls.add(publicPdfPageUrl(fileId, i));
                        }
                    }
                    if (!urls.isEmpty()) {
                        result.put("pageCount", n);
                        result.put("pages", urls);
                        result.put("truncated", false);
                        return result;
                    }
                }
            } catch (Exception ignore) {
                // 缓存损坏，走重新渲染
            }
        }

        // 重新渲染
        deleteDir(dir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("无法创建预览缓存目录");
        }
        int rendered;
        int total;
        try (PDDocument doc = PDDocument.load(src)) {
            total = doc.getNumberOfPages();
            rendered = Math.min(total, PDF_PREVIEW_MAX_PAGES);
            PDFRenderer renderer = new PDFRenderer(doc);
            for (int i = 0; i < rendered; i++) {
                BufferedImage raw = renderer.renderImageWithDPI(i, PDF_PREVIEW_DPI);
                // JPEG 不支持透明通道，统一转 RGB 并铺白底
                BufferedImage rgb = new BufferedImage(raw.getWidth(), raw.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = rgb.createGraphics();
                g.drawImage(raw, 0, 0, Color.WHITE, null);
                g.dispose();
                ImageIO.write(rgb, "jpg", new File(dir, "page-" + (i + 1) + ".jpg"));
            }
        } catch (Exception e) {
            throw new RuntimeException("PDF 渲染失败: " + e.getMessage());
        }
        if (rendered <= 0) {
            throw new RuntimeException("PDF 无可渲染页面");
        }
        try {
            Files.write(meta.toPath(), String.valueOf(rendered).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ignore) {
            // 缓存元数据写入失败不影响本次返回
        }

        for (int i = 1; i <= rendered; i++) {
            urls.add(publicPdfPageUrl(fileId, i));
        }
        result.put("pageCount", rendered);
        result.put("pages", urls);
        result.put("truncated", rendered < total);
        return result;
    }

    private String publicPdfPageUrl(Long fileId, int page) {
        return "/uploads/" + PDF_PREVIEW_DIR + "/" + fileId + "/page-" + page + ".jpg";
    }

    private void deleteDir(File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }
        File[] children = dir.listFiles();
        if (children != null) {
            for (File c : children) {
                deleteDir(c);
            }
        }
        dir.delete();
    }

    private String getExt(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private boolean isPdf(String ext) {
        return "pdf".equals(ext);
    }
}
