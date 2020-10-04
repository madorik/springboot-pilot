package com.estsoft.pilot.app.domain.entity;

import com.estsoft.pilot.app.domain.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "board")
public class BoardEntity extends BaseTimeEntity {
    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("0")
    private Long thread;

    @ColumnDefault("0")
    private int depth;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity userEntity;

    @Column(length = 100, nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @JsonIgnore
    @OneToMany(mappedBy = "boardEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> commentEntities;

    @Builder
    public BoardEntity(Long id, Long thread, int depth, UserEntity userEntity, String subject,
                       String contents, List<CommentEntity> commentEntities) {
        this.id = id;
        this.thread = thread;
        this.depth = depth;
        this.userEntity = userEntity;
        this.subject = subject;
        this.contents = contents;
        this.commentEntities = commentEntities;
    }

    public void update(String subject, String contents) {
        this.subject = subject;
        this.contents = contents;
    }
}
