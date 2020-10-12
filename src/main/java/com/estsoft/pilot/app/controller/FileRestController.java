package com.estsoft.pilot.app.controller;

import com.estsoft.pilot.app.dto.FileDto;
import com.estsoft.pilot.app.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

/**
 * Create by madorik on 2020-09-28
 */
@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/api/v1/images")
public class FileRestController {

    private final FileService fileService;

    private final ResourceLoader resourceLoader;

    /**
     * 이미지 업로드
     *
     * @param file file
     * @return ResponseEntity<String>
     */
    @PostMapping
    public ResponseEntity<Long> upload(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok().body(fileService.store(file).getId());
        } catch (Exception e) {
            log.error("FileException", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 이미지 가져오기
     *
     * @param id id
     * @return ResponseEntity<?>
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getImage(@PathVariable Long id) {
        try {
            FileDto fileDto = fileService.findById(id);
            Resource resource = resourceLoader.getResource("file:" + fileDto.getFilePath());
            return ResponseEntity.ok().body(resource);
        } catch (Exception e) {
            log.error("FileException", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 이미지 삭제
     *
     * @param id id
     * @return ResponseEntity<?>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeImage(@PathVariable Long id) throws FileNotFoundException {
        fileService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
