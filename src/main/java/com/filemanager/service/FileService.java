package com.filemanager.service;

import com.filemanager.model.FileEntity;
import com.filemanager.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private final S3Client s3Client;
    private final String bucketName;
    private final FileRepository fileRepository;

    public FileService(S3Client s3Client, @Value("${aws.s3.bucket}") String bucketName, FileRepository fileRepository) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.fileRepository = fileRepository;
    }

    public List<FileEntity> getAllFilesForUser(String username) {
        return fileRepository.findByUsername(username);
    }

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    public FileEntity uploadFile(MultipartFile file, String username) throws IOException {
        String key = UUID.randomUUID().toString();
        
        // Upload to S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();
        
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        
        // Save metadata to database
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setContentType(file.getContentType());
        fileEntity.setSize(file.getSize());
        fileEntity.setS3Key(key);
        fileEntity.setUsername(username);
        fileEntity.setUploadDate(LocalDateTime.now());
        
        return fileRepository.save(fileEntity);
    }

    public Resource downloadFile(FileEntity file) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getS3Key())
                .build();
        
        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
        byte[] content = s3Object.readAllBytes();
        
        return new ByteArrayResource(content);
    }

    public FileEntity getFileForUser(Long id, String username) {
        return fileRepository.findByIdAndUsername(id, username)
                .orElseThrow(() -> new EntityNotFoundException("File not found"));
    }

    public void deleteFile(Long id, String username) {
        FileEntity file = getFileForUser(id, username);
        
        // Delete from S3
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getS3Key())
                .build();
        
        s3Client.deleteObject(deleteObjectRequest);
        
        // Delete from database
        fileRepository.delete(file);
    }
}
