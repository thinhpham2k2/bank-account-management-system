package com.system.transaction_service.service;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.system.transaction_service.service.interfaces.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {

    @Value("${firebase.image.base.url}")
    private String imageBaseUrl;

    @Override
    public String upload(MultipartFile multipartFile, String fileName) throws IOException {

        InputStream inputStream = multipartFile.getInputStream();
        Bucket bucket = StorageClient.getInstance().bucket();
        bucket.create(fileName, inputStream, "image/" +
                this.getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename())));

        return String.format(imageBaseUrl, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    @Override
    public boolean remove(String fileName) throws IOException {

        Bucket bucket = StorageClient.getInstance().bucket();

        System.out.println(bucket.get(fileName));

        return bucket.get(fileName).delete();
    }

    @Override
    public String download(String fileName) throws IOException {

        return "";
    }

    private String getExtension(String fileName) {

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {

            return fileName.substring(dotIndex + 1);
        }

        return "png";
    }
}
