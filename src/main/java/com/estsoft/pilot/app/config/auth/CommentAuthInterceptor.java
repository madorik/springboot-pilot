package com.estsoft.pilot.app.config.auth;

import com.estsoft.pilot.app.domain.entity.CommentEntity;
import com.estsoft.pilot.app.domain.repository.CommentRepository;
import com.estsoft.pilot.app.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

/**
 * Create by madorik on 2020-10-03
 */
@Component
@AllArgsConstructor
public class CommentAuthInterceptor implements HandlerInterceptor {

    private final CommentRepository commentRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String httpMethod = request.getMethod();

        if (httpMethod.equals("POST") || httpMethod.equals("PUT") || httpMethod.equals("DELETE")) {
            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long id = Long.parseLong((String) pathVariables.get("id"));
            UserDto user = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Optional<CommentEntity> commentEntityWrapper = commentRepository.findById(id);
            if (commentEntityWrapper.isEmpty()) {
                return false;
            }
            String userId = commentEntityWrapper.get().getUserEntity().getEmail();
            if (!userId.equals(user.getEmail())) {
                response.getOutputStream().println("NOT AUTHORIZE!!");
                return false;
            }
        }

        return true;
    }
}
