package io.github.mjyoun.spring.web.aspect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.github.mjyoun.spring.utils.CustomStopWatch;
import io.github.mjyoun.spring.web.annotation.StopWatch;
import io.github.mjyoun.spring.web.annotation.StopWatchRecord;

/**
 * Stopwatch 표시를 위한 Aspect 설정
 * 
 * @author MJ Youn
 * @since 2024. 05. 09.
 */
@Aspect
@Component
public class StopWatchAspect {

    private final Logger logger = LoggerFactory.getLogger(StopWatchAspect.class);

    private final Map<String, CustomStopWatch> stopWatchMap = new HashMap<>();

    /**
     * StopWatch Annotation을 pointcut으로 등록
     * 
     * @author MJ Youn
     * @since 2024. 05. 09.
     */
    @Pointcut("@annotation(io.github.mjyoun.spring.web.annotation.StopWatch)")
    public void enableStopWatch() {};

    /**
     * StopWatchRecord Annotation을 pointcut으로 등록
     * 
     * @author MJ Youn
     * @since 2024. 05. 09.
     */
    @Pointcut("@annotation(io.github.mjyoun.spring.web.annotation.StopWatchRecord)")
    public void enableRecord() {};

    /**
     * Record 시작
     * 
     * @param joinPoint
     *            {@link JoinPoint}
     * 
     * @author MJ Youn
     * @since 2024. 05. 09.
     */
    @Before("enableRecord()")
    public void startRecord(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();

        StopWatchRecord annotation = method.getAnnotation(StopWatchRecord.class);

        if (annotation != null) {
            String threadName = Thread.currentThread().getName();
            CustomStopWatch stopWatch = this.stopWatchMap.get(threadName);

            if (stopWatch != null) {
                if (stopWatch.isRunning()) {
                    stopWatch.stop();
                }

                String name = annotation.taskName() == null ? annotation.value() : annotation.taskName();
                stopWatch.start(name);
            }
        }
    }

    /**
     * Record 종료
     * 
     * @param joinPoint
     *            {@link JoinPoint}
     * 
     * @author MJ Youn
     * @since 2024. 05. 09.
     */
    @After("enableRecord()")
    public void stopRecord(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();

        StopWatchRecord annotation = method.getAnnotation(StopWatchRecord.class);

        if (annotation != null) {
            String threadName = Thread.currentThread().getName();
            CustomStopWatch stopWatch = this.stopWatchMap.get(threadName);

            if (stopWatch != null) {
                if (stopWatch.isRunning()) {
                    stopWatch.stop();
                }
            }
        }
    }

    /**
     * StopWatch 시작
     * 
     * @param joinPoint
     *            {@link JoinPoint}
     * 
     * @author MJ Youn
     * @since 2024. 05. 09.
     */
    @Before("enableStopWatch()")
    public void startStopWatch(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();

        StopWatch annotation = method.getAnnotation(StopWatch.class);

        if (annotation != null) {
            String name = annotation.name() == null ? annotation.value() : annotation.name();
            CustomStopWatch stopWatch = new CustomStopWatch(name, "hh:MM:ss.SSS uuuu");

            String threadName = Thread.currentThread().getName();
            this.stopWatchMap.put(threadName, stopWatch);
        }
    }

    /**
     * StopWatch 종료
     * 
     * @param joinPoint
     *            {@link JoinPoint}
     * 
     * @author MJ Youn
     * @since 2024. 05. 09.
     */
    @After("enableStopWatch()")
    public void finishStopWatch(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();

        StopWatch annotation = method.getAnnotation(StopWatch.class);

        if (annotation != null) {
            String threadName = Thread.currentThread().getName();
            CustomStopWatch stopWatch = this.stopWatchMap.get(threadName);

            if (stopWatch != null) {
                if (stopWatch.isRunning()) {
                    stopWatch.stop();
                }

                logger.debug(stopWatch.prettyPrint());
            }
        }
    }
}
