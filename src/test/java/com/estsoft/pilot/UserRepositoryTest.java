package com.estsoft.pilot;

import com.estsoft.pilot.app.domain.entity.UserEntity;
import com.estsoft.pilot.app.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Create by madorik on 2020-09-22
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @AfterEach
    public void cleanup() {
        userRepository.deleteByEmail("test@test.com");
    }

    @Test
    public void addUsers() {
        userRepository.save(UserEntity.builder()
                .userName("test")
                .password("testpassword")
                .email("test@test.com")
                .build());
    }
}
