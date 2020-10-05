package com.estsoft.pilot.app.domain.entity;

import com.estsoft.pilot.app.domain.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@NamedEntityGraphs({
        @NamedEntityGraph(name = "comment-with-all", attributeNodes = {
                @NamedAttributeNode(value = "boardEntity"),
                @NamedAttributeNode(value = "userEntity")
        })
})
@Entity
@Table(name = "comment")
public class CommentEntity extends BaseTimeEntity {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private BoardEntity boardEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @ColumnDefault("0")
    private Long thread;

    @ColumnDefault("0")
    private int depth;

    @Column(length = 200, nullable = false)
    private String contents;

    private String deleteYn;

    @Builder
    public CommentEntity(Long id, BoardEntity boardEntity, Long thread, int depth, UserEntity userEntity,
                         String contents, String deleteYn) {
        this.id = id;
        this.boardEntity = boardEntity;
        this.thread = thread;
        this.userEntity = userEntity;
        this.depth = depth;
        this.contents = contents;
        this.deleteYn = deleteYn;
    }

    public void update(String contents, String deleteYn) {
        this.contents = contents;
        this.deleteYn = deleteYn;
    }

    public void delete(String deleteYn) {
        this.deleteYn = deleteYn;
    }
}
