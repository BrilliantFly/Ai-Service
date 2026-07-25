package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbImage;
import com.know.knowboot.mapper.knowledge.KbImageMapper;
import com.know.knowboot.service.knowledge.IKbImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 图片服务实现
 */
@Service
public class KbImageServiceImpl extends ServiceImpl<KbImageMapper, KbImage> implements IKbImageService {

    @Autowired
    private KbImageMapper kbImageMapper;

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KbImage upload(byte[] fileBytes, String originalName, String mimeType, Long userId) {
        try {
            // 生成文件路径
            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String ext = originalName != null && originalName.contains(".")
                    ? originalName.substring(originalName.lastIndexOf("."))
                    : ".jpg";
            String fileName = UUID.randomUUID().toString().replace("-", "") + ext;
            String relativePath = "knowledge/" + datePath + "/" + fileName;
            String fullPath = uploadPath + "/" + relativePath;

            // 创建目录
            File dir = new File(uploadPath + "/knowledge/" + datePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 写入文件
            File file = new File(fullPath);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileBytes);
            }

            // 保存记录
            KbImage image = new KbImage();
            image.setOriginalName(originalName);
            image.setFilePath(relativePath);
            image.setFileSize((long) fileBytes.length);
            image.setMimeType(mimeType);
            image.setCreateBy(userId);
            image.setCreateTime(System.currentTimeMillis());
            save(image);

            return image;
        } catch (IOException e) {
            throw new RuntimeException("图片上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public KbImage getById(Long id) {
        return kbImageMapper.selectById(id);
    }
}
