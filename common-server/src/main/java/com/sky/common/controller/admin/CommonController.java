package com.sky.common.controller.admin;

import com.sky.result.Result;
import com.sky.utils.OSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {
    /**
     * 文件上传接口
     * 处理客户端上传的文件，并将其存储到OSS（对象存储服务）中
     *
     * @param file 上传的文件对象，包含文件内容和原始文件名
     * @return Result<String> 包含上传结果的响应对象，成功时返回文件存储路径，失败时返回null
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);
        String path = null;
        try {
            // 验证文件是否存在且具有原始文件名
            if (!file.isEmpty() && file.getOriginalFilename() != null) {
                // 将文件上传到OSS服务并获取存储路径
                path = OSSUtils.upload(file.getBytes(), file.getOriginalFilename());
            }
        } catch (Exception e) {
            log.error("文件上传失败：{}", e.getMessage());
        }
        return Result.success(path);
    }
}