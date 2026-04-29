package io.github.mjyoun.spring.web;

/**
 * component scan에 추가하기 위한 marker class.
 * <p>
 * Spring Boot 2.7 이상 버전에서는 AutoConfiguration이 표준 방식이므로,
 * 이제는 이 Marker 클래스를 사용한 강제 ComponentScan 방식 대신
 * {@code MJSpringWebAutoConfiguration}이 자동으로 빈을 등록합니다.
 * 
 * @author MJ Youn
 * @since 2022. 01. 04.
 * @deprecated Spring Boot AutoConfiguration으로 대체됨
 */
@Deprecated
public class MJComponentScanMarker {

    /**
     * 외부에서 임의 생성을 막기 위한 private constructor
     * 
     * @author MJ Youn
     * @since 2022. 01. 04.
     */
    private MJComponentScanMarker() {
    }

}
