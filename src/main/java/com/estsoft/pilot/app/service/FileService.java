package com.estsoft.pilot.app.service;

import com.estsoft.pilot.app.config.PilotProperties;
import com.estsoft.pilot.app.domain.entity.FileEntity;
import com.estsoft.pilot.app.domain.repository.FileRepository;
import com.estsoft.pilot.app.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

/**
 * Create by madorik on 2020-09-28
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class FileService {

    private final PilotProperties pilotProperties;

    private final FileRepository fileRepository;

    private FileDto convertEntityToDto(FileEntity fileEntity) {
        return FileDto.builder()
                .id(fileEntity.getId())
                .fileName(fileEntity.getFileName())
                .saveFileName(fileEntity.getSaveFileName())
                .filePath(fileEntity.getFilePath())
                .contentType(fileEntity.getContentType())
                .size(fileEntity.getSize())
                .createdDate(fileEntity.getCreatedDate())
                .modifiedDate(fileEntity.getModifiedDate())
                .build();
    }

    /**
     * fileName : test.jpg
     * filePath : d:/images/uuid-test.jpg
     * saveFileName : uuid-test.png
     * contentType : image/jpeg
     * size : 4994942
     *
     * @param file file
     * @return FileEntity
     * @throws IOException IOException
     */
    public FileEntity store(MultipartFile file) throws IOException {
        try {
            if (file.isEmpty()) {
                throw new FileNotFoundException("Failed to store empty file " + file.getOriginalFilename());
            }
            Path rootLocation = Paths.get(pilotProperties.getFileDir());
            String saveFileName = fileSave(rootLocation.toString(), file);
            FileDto fileDto = new FileDto();
            fileDto.setFileName(file.getOriginalFilename());
            fileDto.setSaveFileName(saveFileName);
            fileDto.setContentType(file.getContentType());
            fileDto.setSize(file.getResource().contentLength());
            fileDto.setFilePath(rootLocation.toString().replace(File.separatorChar, '/') + '/' + saveFileName);
            return fileRepository.save(fileDto.toEntity());

        } catch (IOException e) {
            throw new IOException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    /**
     * random uuid를 생성하여 원본 파일명과 혼합하여 리턴
     * @param rootLocation rootLocation
     * @param file file
     * @return saveFileName
     * @throws IOException IOException
     */
    public String fileSave(String rootLocation, MultipartFile file) throws IOException {
        File uploadDir = new File(rootLocation);
        if (!uploadDir.exists()) {
            boolean mkdirs = uploadDir.mkdirs();
            log.info("make dir : {}", mkdirs);
        }

        UUID uuid = UUID.randomUUID();
        String saveFileName = uuid.toString() + file.getOriginalFilename();
        File saveFile = new File(rootLocation, saveFileName);
        FileCopyUtils.copy(file.getBytes(), saveFile);

        return saveFileName;
    }

    /**
     * 이미지 가져오기
     * @param id id
     * @return FileDto
     * @throws Exception Exception
     */
    public FileDto findById(Long id) throws Exception {
        Optional<FileEntity> fileEntityWrapper = fileRepository.findById(id);
        if (fileEntityWrapper.isEmpty()) {
            throw new Exception("Not found file.");
        }
        return this.convertEntityToDto(fileEntityWrapper.get());
    }
}
