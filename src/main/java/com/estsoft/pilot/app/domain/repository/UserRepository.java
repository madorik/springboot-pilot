package com.estsoft.pilot.app.domain.repository;

import com.estsoft.pilot.app.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Create by madorik on 2020-09-21
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    int countByEmail(String email);
}
