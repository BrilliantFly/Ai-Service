package com.know.knowboot.service.knowledge;

import com.know.knowboot.entity.knowledge.KbImage;

/**
 * 图片服务接口
 */
public interface IKbImageService {

    /**
     * 上传图片
     */
    KbImage upload(byte[] fileBytes, String originalName, String mimeType, Long userId);

    /**
     * 获取图片信息
     */
    KbImage getById(Long id);
}
