package com.estsoft.pilot.app.dto;

import com.estsoft.pilot.app.domain.entity.BoardEntity;
import com.estsoft.pilot.app.domain.entity.CommentEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private Long thread;
    private int depth;
    private String userId;
    private String userName;
    private String subject;
    private String contents;
    private List<CommentEntity> commentEntities;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public BoardDto(Long id) {
        this.id = id;
    }

    public BoardEntity toEntity() {
        BoardEntity boardEntity = BoardEntity.builder()
                .id(id)
                .thread(thread)
                .depth(depth)
                .userId(userId)
                .userName(userName)
                .subject(subject)
                .contents(contents)
                .commentEntities(commentEntities)
                .build();

        return boardEntity;
    }

    @Builder
    public BoardDto(Long id, Long thread, int depth, String userId, String userName, String subject,
                    String contents, List<CommentEntity> commentEntities, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.thread = thread;
        this.depth = depth;
        this.userId = userId;
        this.userName = userName;
        this.subject = subject;
        this.contents = contents;
        this.commentEntities = commentEntities;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
