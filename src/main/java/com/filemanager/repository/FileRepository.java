package com.filemanager.repository;

import com.filemanager.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByUsername(String username);
    Optional<FileEntity> findByIdAndUsername(Long id, String username);
}
