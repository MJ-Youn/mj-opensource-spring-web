package io.github.mjyoun.spring.web.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

import io.github.mjyoun.spring.web.aspect.HttpRequestLogAspect;
import io.github.mjyoun.spring.web.aspect.StopWatchAspect;
import io.github.mjyoun.spring.web.error.CustomErrorController;
import io.github.mjyoun.spring.web.filter.RestRequestFilter;
import io.github.mjyoun.spring.web.service.CSVService;
import io.github.mjyoun.spring.web.service.DownloadService;

/**
 * Spring Web 모듈의 컴포넌트들을 등록하기 위한 Auto Configuration 클래스. 기존 MJComponentScanMarker 방식을 대체합니다.
 * 
 * @author MJ Youn
 * @since 2026. 04. 29.
 */
@AutoConfiguration
@Import({ //
        CustomErrorController.class, //
        HttpRequestLogAspect.class, //
        RestRequestFilter.class, //
        StopWatchAspect.class, //
        DownloadService.class, //
        CSVService.class //
})
public class MJSpringWebAutoConfiguration {

}
