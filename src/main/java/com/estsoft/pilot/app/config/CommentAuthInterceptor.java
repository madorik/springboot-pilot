package com.estsoft.pilot.app.config;

import com.estsoft.pilot.app.domain.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by madorik on 2020-10-03
 */
@Component
@AllArgsConstructor
public class CommentAuthInterceptor implements HandlerInterceptor {
    private CommentRepository commentRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String httpMethod = request.getMethod();

        /*if (httpMethod.equals("POST") || httpMethod.equals("PUT") || httpMethod.equals("DELETE")) {
            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long id = Long.parseLong((String) pathVariables.get("id"));
            UserDto user = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Optional<CommentEntity> commentEntityWrapper = commentRepository.findById(id);
            String userId = commentEntityWrapper.get().getUserEntity().getEmail();
            if (!userId.equals(user.getEmail())) {
                response.getOutputStream().println("NOT AUTHORIZE!!");
                return false;
            }
        }*/

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
