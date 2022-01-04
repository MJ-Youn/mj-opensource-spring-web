package io.github.mjyoun.spring.web.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

/**
 * HTTP호출에 따른 로그를 출력하기 위한 LogAspect 설정
 * 
 * @author MJ Youn
 * @since 2022. 01. 04.
 */
@Aspect
@Component
public class HttpRequestLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestLogAspect.class);

    /**
     * RequestMapping Annotation을 pointcut으로 등록
     * 
     * @author MJ Youn
     * @since 2022. 01. 04.
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMapping() {
    };

    /**
     * requestMapping() method 호출 전에 호출되는 메소드
     * 
     * @param joinPoint
     *            호출 정보
     * 
     * @author MJ Youn
     * @since 2022. 01. 04.
     */
    @Before("requestMapping()") // pointcut으로 등록된 requestMapping 정보를 호출하기 위한 설정
    public void printLogBeforeController(JoinPoint joinPoint) {
        // 요청 정보
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        logger.info(this.getUrlInfo(request));
    }

    /**
     * request 정보를 추출하여 문자열로 출력하는 함수
     * 
     * @param request
     *            요청 정보
     * @return request 정보 문자열
     * 
     * @author MJ Youn
     * @since 2022. 01. 04.
     */
    private String getUrlInfo(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();

        sb.append("Session: ");
        sb.append(request.getSession().getId());
        sb.append(", ");
        sb.append("Remote: ");
        sb.append(request.getRemoteAddr());
        sb.append(" (");
        sb.append(request.getRemoteHost());
        sb.append("), URL-Pattern: ");
        sb.append(request.getMethod());
        sb.append(" | ");
        sb.append((String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));
        sb.append((request.getQueryString() != null ? "?" + request.getQueryString() : ""));
        sb.append(", URL-Variables: ");
        sb.append(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
        sb.append(", URL: ");
        sb.append(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));

        return sb.toString();
    }

}
