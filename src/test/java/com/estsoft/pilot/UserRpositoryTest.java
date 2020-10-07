package com.estsoft.pilot;

import com.estsoft.pilot.app.domain.repository.UserRepository;
import groovy.util.logging.Log;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

/**
 * Create by madorik on 2020-10-02
 */
@SpringBootTest
@Log
@Commit
public class UserRpositoryTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Test
    public void get() {

       /* Page<UserEntity> userEntity = userRepository.findNative(PageRequest.of(0, 20));

        logger.info(userEntity.toString());*/

    }
}
