package com.estsoft.pilot.app.domain.entity;

import com.estsoft.pilot.app.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "board")
public class BoardEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long pid;

    @ColumnDefault("0")
    private int orderNo;

    @ColumnDefault("0")
    private int depth;

    private String userId;

    @Column(length = 10, nullable = false, updatable=false)
    private String userName;

    @Column(length = 100, nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @Builder
    public BoardEntity(Long id, Long pid, int orderNo, int depth, String userId, String userName, String subject, String contents) {
        this.id = id;
        this.pid = pid;
        this.orderNo = orderNo;
        this.depth = depth;
        this.userId = userId;
        this.userName = userName;
        this.subject = subject;
        this.contents = contents;
    }

    public void update(String subject, String contents) {
        this.subject = subject;
        this.contents = contents;
    }
}
