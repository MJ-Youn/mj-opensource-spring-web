package io.github.mjyoun.spring.web.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 보안 관련 유틸리티
 * 
 * @author MJ Youn
 * @since 2022. 10. 06.
 */
public class SecurityUtils {

    public static String getCurrentUserId() {
        String userId = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 로그인을 했을 경우
        if (auth != null && UsernamePasswordAuthenticationToken.class.isAssignableFrom(auth.getClass())) {
            userId = auth.getPrincipal().toString();
        }

        return userId;
    }

}
