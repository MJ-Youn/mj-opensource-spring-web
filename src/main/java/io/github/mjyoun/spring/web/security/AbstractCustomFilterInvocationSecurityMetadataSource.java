package io.github.mjyoun.spring.web.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * url 및 페이지 접근 가능 여부 판단 로직
 * 
 * @author MJ Youn
 * @since 2022. 02. 21.
 */
@Slf4j
public abstract class AbstractCustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    /**
     * 권한 필요없이 접근 가능한 페이지 목록을 조회하는 함수
     * 
     * @return 권한 필요없이 접근 가능한 페이지 목록
     * 
     * @author MJ Youn
     * @since 2022. 02. 25.
     */
    protected abstract String[] getAllAccessPages();

    /**
     * 권한이 없을 경우에만 접근 가능한 페이지 목록을 조회하는 함수
     * 
     * @return 권한이 없을 경우에만 접근 가능한 페이지 목록
     * 
     * @author MJ Youn
     * @since 2022. 02. 25.
     */
    protected abstract String[] getAnonymousePages();

    /**
     * 권한 확인이 필요한 API인지 확인하는 함수
     * 
     * @param method
     *            API의 Method
     * @param url
     *            API의 URL
     * @return true일 경우, 권한 확인이 필요한 API. 아닐 경우 권한 확인이 필요없는 API
     * 
     * @author MJ Youn
     * @since 2022. 02. 21.
     */
    protected abstract boolean isRequiredPermissionUrl(String method, String url);

    /**
     * API 호출이 가능한 권한 목록을 조회하는 함수
     * 
     * @param method
     *            API의 Method
     * @param url
     *            API의 URL
     * @return 해당 API를 호출할 수 있는 권한 목록
     */
    protected abstract List<ConfigAttribute> getRolesAccessedApi(String method, String url);

    /**
     * @see FilterInvocationSecurityMetadataSource#getAttributes(Object)
     * 
     * @author MJ Youn
     * @since 2022. 02. 21.
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        List<ConfigAttribute> attrs = null;
        FilterInvocation fi = (FilterInvocation) object;
        HttpServletRequest request = fi.getHttpRequest();

        String method = request.getMethod();
        String url = request.getRequestURI();

        try {
            String contentType = request.getContentType();
            String remoteAttr = request.getRemoteAddr();

            log.debug("{}{} {}, from: {}", method, contentType == null ? "" : " " + contentType, url, remoteAttr);
        } catch (Exception e) {
            log.debug("{} {}", method, url);
        }

        // 페이지에 대한 접근 인지 확인
        if (this.isAllAccessPage(url)) {
            // 페이지에 대한 요청일 경우 vote를 타지 않기 위해 attrs를 Null로 설정
            attrs = null;
        } else if (this.isAnonymousePage(url)) {
            attrs = new ArrayList<>();
        } else {
            // 권한 확인이 필요한 요청인지 확인
            if (this.isRequiredPermissionUrl(method, url)) {
                attrs = this.getRolesAccessedApi(method, url);
            } else {
                attrs = null;
            }
        }

        return attrs;
    }

    /**
     * @see FilterInvocationSecurityMetadataSource#getAllConfigAttributes()
     * 
     * @author MJ Youn
     * @since 2022. 02. 21.
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * @see FilterInvocationSecurityMetadataSource#supports(Class)
     * 
     * @author MJ Youn
     * @since 2022. 02. 21.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    /**
     * 권한에 상관 없이 접근 가능한 페이지인지 확인하는 함수
     * 
     * @param url
     *            접속한 페이지 정보
     * @return true일 경우 모두 접근 가능한 페이지
     * 
     * @author MJ Youn
     * @since 2022. 03. 15.
     */
    private boolean isAllAccessPage(String url) {
        return Arrays.asList(this.getAllAccessPages()) //
                .parallelStream() //
                .filter(page -> StringUtils.equals(page, url)) //
                .findAny() //
                .orElse(null) != null;
    }

    /**
     * 로그인하지 않는 유저만 접근 가능한 페이지인지 확인하는 함수
     * 
     * @param url
     *            접속한 페이지 정보
     * @return true일 경우 로그인하지 않은 유저만 접근 가능한 페이지
     * 
     * @author MJ Youn
     * @since 2022. 03. 15.
     */
    private boolean isAnonymousePage(String url) {
        return Arrays.asList(this.getAnonymousePages()) //
                .parallelStream() //
                .filter(page -> StringUtils.equals(page, url)) //
                .findAny() //
                .orElse(null) != null;
    }

    /**
     * 권한 목록을 securit에서 사용하기 위한 {@link ConfigAttribute} 목록으로 변환하는 함수
     * 
     * @param <T>
     *            {@link AbstractGrade}
     * @param grades
     *            권한 목록
     * @return {@link ConfigAttribute} 목록
     * 
     * @author MJ Youn
     * @since 2022. 03. 15.
     */
    protected <T extends AbstractGrade> List<ConfigAttribute> convert(List<T> grades) {
        if (grades == null) {
            return new ArrayList<>();
        } else {
            return grades //
                    .parallelStream() //
                    .map(g -> new SecurityConfig(g.getId())) //
                    .collect(Collectors.toList());
        }
    }

}
