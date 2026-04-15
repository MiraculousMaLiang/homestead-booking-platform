package com.homestead.booking.service.impl;


import com.homestead.booking.service.FileUpdateService;
import com.homestead.booking.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class FileUpdateServiceImpl implements FileUpdateService {
    @Autowired
    private AliOssUtil aliOssUtil; // 注入AliOssUtil实例
    @Override
    public String upload(MultipartFile file) throws Exception {
        String originalFilename =file.getOriginalFilename();
        String filename= UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
        String contentType = file.getContentType();
        //保证文件为唯一
//        file.transferTo(new File("E:\\rog图片\\"+filename));
        String url= aliOssUtil.uploadFile(filename,file.getInputStream(),contentType);
        return url;
    }
}
