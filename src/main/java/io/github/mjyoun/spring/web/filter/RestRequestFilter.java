package io.github.mjyoun.spring.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * log에 찍히는 http thread name을 변경해주기 위한 filter security보다 filter 순위를 먼저 두어서 실행해야한다.
 * 
 * @author MJ Youn
 * @since 2022. 01. 04.
 */
@Order(0)
@Component
public class RestRequestFilter implements Filter {

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     * 
     * @author MJ Youn
     * @since 2022. 01. 04.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // HTTP 호출 Thread의 이름을 변경. HTTP-XX
        Thread.currentThread().setName(String.format("HTTP-%02d", Thread.currentThread().getId()));
        chain.doFilter(request, response);
    }

}
