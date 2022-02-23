package io.github.mjyoun.springsecurity.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
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

    protected abstract String[] getAllAccessPages();

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
     * @param <T>
     *            권한 객체 정보. {@link ConfigAttribute}를 상속 받은 객체여야함
     * @param method
     *            API의 Method
     * @param url
     *            API의 URL
     * @return 해당 API를 호출할 수 있는 권한 목록
     */
    protected abstract <T extends ConfigAttribute> List<T> getRolesAccessedApi(String method, String url);

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
        String contentType = request.getContentType();
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        String remoteAttr = request.getRemoteAddr();

        log.debug("{}{} {}{}, from: {}", method, contentType == null ? "" : " " + contentType, url, contextPath, remoteAttr);

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

    private boolean isAllAccessPage(String url) {
        return Arrays.asList(this.getAllAccessPages()) //
                .parallelStream() //
                .filter(page -> StringUtils.equals(page, url)) //
                .findAny() //
                .orElse(null) != null;
    }

    private boolean isAnonymousePage(String url) {
        return Arrays.asList(this.getAnonymousePages()) //
                .parallelStream() //
                .filter(page -> StringUtils.equals(page, url)) //
                .findAny() //
                .orElse(null) != null;
    }

}
