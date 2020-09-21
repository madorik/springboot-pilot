package com.estsoft.pilot.app.dto;

import com.estsoft.pilot.app.domain.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Create by madorik on 2020-09-21
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String email;
    private String password;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(id)
                .userName(userName)
                .email(email)
                .password(password)
                .build();
    }

    @Builder
    public UserDto(Long id, String userName, String email, String password) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

}
