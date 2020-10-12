package com.estsoft.pilot.app.domain.entity;

import com.estsoft.pilot.app.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Create by madorik on 2020-09-28
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "files")
public class FileEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fileName;

    @Column
    private String saveFileName;

    @Column
    private String filePath;

    @Column
    private String contentType;

    @Column
    private Long size;

    @Column
    private Long boardId;

    @Column
    private Long commentId;


    @Builder
    public FileEntity(Long id, String fileName, String saveFileName, String filePath,
                      String contentType, Long size, Long boardId, Long commentId) {
        this.id = id;
        this.fileName = fileName;
        this.saveFileName = saveFileName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.size = size;
        this.boardId = boardId;
        this.commentId = commentId;
    }
}
