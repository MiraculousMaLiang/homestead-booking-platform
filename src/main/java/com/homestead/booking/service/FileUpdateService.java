package com.homestead.booking.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUpdateService {
    String upload(MultipartFile file) throws Exception;
}
