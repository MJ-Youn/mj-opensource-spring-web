package io.github.mjyoun.spring.web.security;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

/**
 * url 및 페이지 접근 가능 여부 판단 로직 (Spring Security 7+ AuthorizationManager)
 * 
 * @author MJ Youn
 * @since 2026. 03. 16.
 */
public abstract class AbstractCustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    protected static final Logger log = LoggerFactory.getLogger(AbstractCustomAuthorizationManager.class);

    /**
     * 권한 필요없이 접근 가능한 페이지 목록을 조회하는 함수
     */
    protected abstract String[] getAllAccessPages();

    /**
     * 권한이 없을 경우에만 접근 가능한 페이지 목록을 조회하는 함수
     */
    protected abstract String[] getAnonymousePages();

    /**
     * 권한 확인이 필요한 API인지 확인하는 함수
     */
    protected abstract boolean isRequiredPermissionUrl(String method, String url);

    /**
     * API 호출이 가능한 권한 목록을 조회하는 함수
     */
    protected abstract List<String> getRolesAccessedApi(String method, String url);

    /**
     * 권한 목록을 문자열 Role 리스트로 변환하는 함수
     * 
     * @param <T> {@link AbstractGrade}
     * @param grades 권한 목록
     * @return Role 문자열 목록
     */
    protected <T extends AbstractGrade> List<String> convert(List<T> grades) {
        if (grades == null) {
            return new ArrayList<>();
        } else {
            return grades
                    .parallelStream()
                    .map(AbstractGrade::getId)
                    .collect(Collectors.toList());
        }
    }

}
