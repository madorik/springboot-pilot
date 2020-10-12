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
    private Long boardId;
    private Long commentId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public FileEntity toEntity() {
        return FileEntity.builder()
                .id(id)
                .fileName(fileName)
                .saveFileName(saveFileName)
                .filePath(filePath)
                .contentType(contentType)
                .size(size)
                .boardId(boardId)
                .commentId(commentId)
                .build();
    }

    @Builder
    public FileDto(Long id, String fileName, String saveFileName, String filePath, String contentType, Long size,
                   Long boardId, Long commentId, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.fileName = fileName;
        this.saveFileName = saveFileName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.size = size;
        this.boardId = boardId;
        this.commentId = commentId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
