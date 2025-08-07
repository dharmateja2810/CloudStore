package com.filemanager.controller;

import com.filemanager.model.FileEntity;
import com.filemanager.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping
    public String listFiles(Model model, Principal principal) {
        List<FileEntity> files = fileService.getAllFilesForUser(principal.getName());
        model.addAttribute("files", files);
        return "files";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                           Principal principal,
                           RedirectAttributes redirectAttributes) {
        try {
            fileService.uploadFile(file, principal.getName());
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Failed to upload file: " + e.getMessage());
        }
        return "redirect:/files";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, Principal principal) {
        try {
            FileEntity file = fileService.getFileForUser(id, principal.getName());
            Resource resource = fileService.downloadFile(file);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteFile(@PathVariable Long id,
                           Principal principal,
                           RedirectAttributes redirectAttributes) {
        try {
            fileService.deleteFile(id, principal.getName());
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "File deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Failed to delete file: " + e.getMessage());
        }
        return "redirect:/files";
    }
}
