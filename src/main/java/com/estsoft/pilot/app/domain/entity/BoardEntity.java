package com.estsoft.pilot.app.domain.entity;

import com.estsoft.pilot.app.domain.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@NamedEntityGraph(name = "board-with-user", attributeNodes = {
        @NamedAttributeNode(value = "userEntity"),
})
@Entity
@Table(name = "board")
public class BoardEntity extends BaseTimeEntity {
    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("0")
    private Long thread;

    @ColumnDefault("0")
    private Integer depth;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(length = 100, nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @Column(columnDefinition = "varchar(1) default 'N'")
    private String deleteYn;

    @JsonIgnore
    @OneToMany(mappedBy = "boardEntity", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CommentEntity> commentEntities;

    @Builder
    public BoardEntity(Long id, Long thread, int depth, UserEntity userEntity, String subject,
                       String contents, List<CommentEntity> commentEntities, String deleteYn) {
        this.id = id;
        this.thread = thread;
        this.depth = depth;
        this.userEntity = userEntity;
        this.subject = subject;
        this.contents = contents;
        this.commentEntities = commentEntities;
        this.deleteYn = deleteYn;
    }

    @PrePersist
    public void prePersist() {
        if (this.deleteYn == null) {
            this.deleteYn = "N";
        }
    }

    public void update(String subject, String contents) {
        this.subject = subject;
        this.contents = contents;
    }
}
