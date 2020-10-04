package com.estsoft.pilot.app.dto;

import com.estsoft.pilot.app.domain.entity.BoardEntity;
import com.estsoft.pilot.app.domain.entity.CommentEntity;
import com.estsoft.pilot.app.domain.entity.UserEntity;
import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private Long thread;
    private int depth;
    private UserEntity userEntity;
    private String subject;
    private String contents;
    private List<CommentEntity> commentEntities;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public BoardDto(Long id) {
        this.id = id;
    }

    public BoardEntity toEntity() {
        UserDto userDto = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BoardEntity boardEntity = BoardEntity.builder()
                .id(id)
                .thread(thread)
                .depth(depth)
                .userEntity(userDto.toEntity())
                .subject(subject)
                .contents(contents)
                .commentEntities(commentEntities)
                .build();

        return boardEntity;
    }

    @Builder
    public BoardDto(Long id, Long thread, int depth, UserEntity userEntity, String userName, String subject,
                    String contents, List<CommentEntity> commentEntities, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.thread = thread;
        this.depth = depth;
        this.userEntity = userEntity;
        this.subject = subject;
        this.contents = contents;
        this.commentEntities = commentEntities;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
