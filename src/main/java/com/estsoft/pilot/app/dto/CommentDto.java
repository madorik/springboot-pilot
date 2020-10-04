package com.estsoft.pilot.app.dto;

import com.estsoft.pilot.app.domain.entity.BoardEntity;
import com.estsoft.pilot.app.domain.entity.CommentEntity;
import com.estsoft.pilot.app.domain.entity.UserEntity;
import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

/**
 * Create by madorik on 2020-10-01
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private BoardEntity boardEntity;
    private Long thread;
    private int depth;
    private UserEntity userEntity;
    private String userName;
    private String subject;
    private String contents;
    private String deleteYn;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public CommentEntity toEntity() {
        UserDto userDto = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CommentEntity commentEntity = CommentEntity.builder()
                .id(id)
                .boardEntity(boardEntity)
                .thread(thread)
                .depth(depth)
                .userEntity(userDto.toEntity())
                .contents(contents)
                .deleteYn(deleteYn)
                .build();

        return commentEntity;
    }

    @Builder
    public CommentDto(Long id, BoardEntity boardEntity, Long thread, int depth, UserEntity userEntity, String userName,
                    String contents, String deleteYn, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.boardEntity = boardEntity;
        this.thread = thread;
        this.depth = depth;
        this.userEntity = userEntity;
        this.userName = userName;
        this.contents = contents;
        this.deleteYn = deleteYn;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
