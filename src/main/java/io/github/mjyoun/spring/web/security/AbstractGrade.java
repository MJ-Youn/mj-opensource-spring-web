package io.github.mjyoun.spring.web.security;

/**
 * 권한 정보를 출력하기 위한 객체
 * 
 * @author MJ Youn
 * @since 2022. 02. 21.
 */
public abstract class AbstractGrade {

    /**
     * 권한을 구분하기 위한 식별자 정보
     * 
     * @return 권한 식별자
     * 
     * @author MJ Youn
     * @since 2022. 02. 21.
     */
    public abstract String getId();

}
