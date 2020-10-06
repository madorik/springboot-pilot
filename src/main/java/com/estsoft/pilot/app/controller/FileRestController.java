package com.estsoft.pilot.app.controller;

import com.estsoft.pilot.app.dto.FileDto;
import com.estsoft.pilot.app.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Create by madorik on 2020-09-28
 */
@AllArgsConstructor
@RestController
@Slf4j
public class FileRestController {

    private final FileService fileService;

    private final ResourceLoader resourceLoader;

    /**
     * 이미지 업로드
     * @param file
     * @return
     */
    @PostMapping("/images")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok().body("/images/" + fileService.store(file).getId());
        } catch(Exception e) {
            log.error("FileException", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 이미지 가져오기
     * @param id
     * @return
     */
    @GetMapping("/images/{id}")
    public ResponseEntity<?> getImage(@PathVariable Long id){
        try {
            FileDto fileDto = fileService.findById(id);
            Resource resource = resourceLoader.getResource("file:" + fileDto.getFilePath());
            return ResponseEntity.ok().body(resource);
        } catch(Exception e) {
            log.error("FileException", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
