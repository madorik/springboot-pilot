package com.estsoft.pilot.app.domain.repository;

import com.estsoft.pilot.app.domain.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

}
