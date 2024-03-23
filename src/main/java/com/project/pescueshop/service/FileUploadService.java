package com.project.pescueshop.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService extends BaseService {
    private final Cloudinary cloudinaryConfig;

    public String uploadFile(MultipartFile file, String path, String prefix) {
        try {
            File uploadedFile = convertMultiPartToFile(file);
            Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, Map.of("public_id", path + "/" + prefix + "_" + UUID.randomUUID()));
            boolean isDeleted = uploadedFile.delete();

            if (isDeleted){
                log.trace("File successfully deleted");
            }else
                log.trace("File doesn't exist");
            return  uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private List<File> convertMultipartToFiles(MultipartFile[] files) throws IOException {
        return Arrays.stream(files)
                .map(file -> {
                    try {
                        return convertMultiPartToFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).toList();
    }
}
