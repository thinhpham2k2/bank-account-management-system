package com.system.transaction_service.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String upload(MultipartFile multipartFile, String fileName) throws IOException;

    boolean remove(String fileName) throws IOException;

    String download(String fileName) throws IOException;
}
