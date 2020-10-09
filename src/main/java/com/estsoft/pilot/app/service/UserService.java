package com.estsoft.pilot.app.service;

import com.estsoft.pilot.app.config.PilotProperties;
import com.estsoft.pilot.app.domain.entity.Role;
import com.estsoft.pilot.app.domain.entity.UserEntity;
import com.estsoft.pilot.app.domain.repository.UserRepository;
import com.estsoft.pilot.app.dto.UserDto;
import com.estsoft.pilot.app.util.ApplicationContextUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Create by madorik on 2020-09-21
 */
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PilotProperties pilotProperties;

    private final LoginAttemptService loginAttemptService;

    private final HttpServletRequest request;

    private UserDto convertEntityToDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .userName(userEntity.getUserName())
                .password(userEntity.getPassword())
                .build();
    }

    /**
     * UserDetails를 구현한 User를 반환한다.
     * @param email email
     * @return UserDetails
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }

        Optional<UserEntity> optUserEntity = userRepository.findByEmail(email);
        if (optUserEntity.isEmpty()) {
            throw new UsernameNotFoundException("User not found with this email : " + email);
        }

        UserEntity userEntity = optUserEntity.get();
        List<GrantedAuthority> authorityList = new ArrayList<>();

        if (pilotProperties.getDefaultUserEmail().equals(email)) {
            authorityList.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorityList.add(new SimpleGrantedAuthority(Role.USER.getValue()));
        }

        UserDto userDto = convertEntityToDto(userEntity);
        userDto.setAuthorities(authorityList);
        return userDto;
    }

    /**
     * 회원가입 - 비밀번호 암호화
     * @param userDto userDto
     */
    @Transactional
    public void joinUser(UserDto userDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = (BCryptPasswordEncoder) ApplicationContextUtils.getBean("passwordEncoder");
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        userRepository.save(userDto.toEntity());
    }

    /**
     * 이메일 중복 체크
     * @param email email
     * @return email count
     */
    @Transactional
    public int checkInvalidEmail(String email) {
        return userRepository.countByEmail(email);
    }

    /**
     * Request IP 확인
     * @return 요청 IP
     */
    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}
