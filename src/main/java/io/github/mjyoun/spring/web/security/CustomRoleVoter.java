package io.github.mjyoun.spring.web.security;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 현재 로그인한 유저의 권한과 요청한 데이터의 권한을 비교하여 접속 가능한지 확인하는 함수
 * 
 * @author MJ Youn
 * @since 2022. 02. 21.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomRoleVoter extends RoleVoter {

    /** role 접두사 */
    private String rolePrefix = "";

    /**
     * @see RoleVoter#vote(Authentication, Object, Collection)
     * 
     * @author MJ Youn
     * @since 2022. 02. 21.
     */
    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        // authentication이 null일 경우 권한 없음
        if (authentication == null) {
            return ACCESS_DENIED;
        }

        int result = ACCESS_ABSTAIN;

        // 로그인한 유저의 권한 목록
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;

                for (GrantedAuthority authority : authorities) {
                    if (authority.getAuthority() != null) {

                        boolean authHas = Arrays.asList(authority.getAuthority().split(",")).stream() //
                                .map(auth -> StringUtils.equals(auth, attribute.getAttribute())) //
                                .findAny() //
                                .orElse(false);

                        if (authHas) {
                            return ACCESS_GRANTED;
                        }
                    }
                }
            }
        }

        return result;
    }

}
