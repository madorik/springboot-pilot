package com.estsoft.pilot.app.dto;

import com.estsoft.pilot.app.domain.entity.BoardEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private Long pid;
    private int orderNo;
    private int depth;
    private String userId;
    private String userName;
    private String subject;
    private String contents;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public BoardEntity toEntity() {
        BoardEntity boardEntity = BoardEntity.builder()
                .id(id)
                .pid(pid)
                .orderNo(orderNo)
                .depth(depth)
                .userId(userId)
                .userName(userName)
                .subject(subject)
                .contents(contents)
                .build();

        return boardEntity;
    }

    @Builder
    public BoardDto(Long id, Long pid, int orderNo, int depth, String userId, String userName, String subject,
                    String contents, Long hits, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.pid = pid;
        this.orderNo = orderNo;
        this.depth = depth;
        this.userId = userId;
        this.userName = userName;
        this.subject = subject;
        this.contents = contents;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

}
