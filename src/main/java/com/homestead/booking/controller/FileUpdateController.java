package com.homestead.booking.controller;


import com.homestead.booking.common.Result;
import com.homestead.booking.service.FileUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUpdateController {
    @Autowired
    private FileUpdateService fileService;
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        String url = fileService.upload(file);
        return Result.success(url);
    }
}
