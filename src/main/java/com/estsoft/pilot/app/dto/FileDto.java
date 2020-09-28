package com.estsoft.pilot.app.dto;

import com.estsoft.pilot.app.domain.entity.FileEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileDto {
    private Long id;
    private String fileName;
    private String saveFileName;
    private String filePath;
    private String contentType;
    private Long size;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public FileEntity toEntity() {
        FileEntity fileEntity = FileEntity.builder()
                .id(id)
                .fileName(fileName)
                .saveFileName(saveFileName)
                .filePath(filePath)
                .contentType(contentType)
                .size(size)
                .build();
        return fileEntity;
    }

    @Builder
    public FileDto(Long id, String fileName, String saveFileName, String filePath, String contentType, Long size,
                   LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.fileName = fileName;
        this.saveFileName = saveFileName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.size = size;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
