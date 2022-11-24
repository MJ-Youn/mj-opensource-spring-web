package io.github.mjyoun.spring.web.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Download 기능 관련된 유틸리티 클래스
 * 
 * @author MJ Youn
 * @since 2022. 06. 21.
 */
@Slf4j
@Service(DownloadService.QUALIFIER_NAME)
public class DownloadService {

    public static final String QUALIFIER_NAME = "io.github.mjyoun.spring.web.service.DownloadService";

    private CSVService csvService;

    /**
     * (non-javadoc)
     * 
     * @param csvService
     *            {@link CSVService}
     * 
     * @author MJ Youn
     * @since 2022. 06. 21.
     */
    protected DownloadService(@Qualifier(CSVService.QUALIFIER_NAME) CSVService csvService) {
        this.csvService = csvService;
    }

    /**
     * @see DownloadService#downloadCsv(String, String[], List, char, char, HttpServletResponse)
     * 
     * @param fileName
     *            확장자를 제외한 파일 이름 (확장자는 '.csv' 형태로 자동으로 붙혀줌)
     * @param headers
     *            헤더 목록. null일 경우 헤더가 없는 형태의 csv 파일
     * @param datas
     *            데이터 목록
     * @param response
     *            {@link HttpServletResponse}
     * @throws IOException
     *             FileWriter 닫는중에 오류 발생
     * 
     * @author MJ Youn
     * @since 2022. 06. 21.
     */
    public void downloadCSV(@NotNull String fileName, String[] headers, List<String[]> datas, HttpServletResponse response) throws IOException {
        this.downloadCsv(fileName, headers, datas, ',', '\0', response);
    }

    /**
     * Array 데이터를 기준으로 CSV 파일로 다운로드해주는 함수
     * 
     * @param fileName
     *            확장자를 제외한, 다운로드할 파일 이름 (확장자는 자동으로 붙혀줌)
     * @param headers
     *            헤더 목록. null일 경우 헤더가 없는 형태의 csv 파일
     * @param datas
     *            데이터 목록
     * @param separator
     *            구분자
     * @param quote
     *            따옴표, 없을 경우 따옴표 하지 않음. 있을 경우 무조건 따옴표로 묶음
     * @param response
     *            {@link HttpServletResponse}
     * 
     * @throws IOException
     *             FileWriter 닫는중에 오류 발생
     * 
     * @author MJ Youn
     * @since 2022. 06. 21.
     */
    public void downloadCsv(@NotNull String fileName, String[] headers, List<String[]> datas, char separator, char quote, HttpServletResponse response) throws IOException {
        final String methodName = "DownloadService#downloadCSV";

        fileName = new StringBuffer(fileName).append(".csv").toString();
        log.debug("[{}] 다운로드 파일 이름: {}", methodName, fileName);

        // csv content 생성
        byte[] contentBytes = this.csvService.createCSV(fileName, headers, datas, separator, quote);
        InputStream inputStream = new ByteArrayInputStream(contentBytes);

        // download를 위한 disposition 생성
        String contentDisposition = new StringBuffer("attachment; filename=\"") //
                .append(URLEncoder.encode(fileName, "UTF-8").replace("+", "%20")) //
                .append("\"") //
                .toString();

        try {
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.setContentLength(contentBytes.length);
            response.setHeader("Content-Disposition", contentDisposition);

            FileCopyUtils.copy(inputStream, response.getOutputStream());
            log.debug("[{}] CSV 파일 다운로드 요청 성공 [file name: {}]", methodName, fileName);
        } catch (IOException ioe) {
            log.error("[{}] CSV 파일 다운로드 실패 [msg: {}]", methodName, ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    /**
     * 파일 다운로드
     * 
     * @param filePath
     *            파일 경로. 저장되어 있는 파일 이름으로 다운로드 요청
     * @param response
     *            {@link HttpServletResponse}
     * 
     * @throws FileNotFoundException
     *             다운로드할 파일에 문제가 있을 경우
     * @throws UnsupportedEncodingException
     *             파일 이름 인코딩 설정이 잘못 되었을 경우. 발생하지 않을 듯..
     * 
     * @author MJ Youn
     * @since 2022. 08. 12.
     */
    public void downloadFile(@NotNull Path filePath, @NotNull HttpServletResponse response) throws FileNotFoundException, UnsupportedEncodingException {
        this.downloadFile(filePath.toFile().getName(), filePath, response);
    }

    /**
     * 파일 다운로드
     * 
     * @param downloadFileName
     *            다운로드할 파일의 이름
     * @param filePath
     *            파일 경로
     * @param response
     *            {@link HttpServletResponse}
     * 
     * @throws FileNotFoundException
     *             다운로드할 파일에 문제가 있을 경우
     * @throws UnsupportedEncodingException
     *             파일 이름 인코딩 설정이 잘못 되었을 경우. 발생하지 않을 듯..
     * 
     * @author MJ Youn
     * @since 2022. 08. 12.
     */
    public void downloadFile(@NotBlank String downloadFileName, @NotNull Path filePath, @NotNull HttpServletResponse response) throws FileNotFoundException, UnsupportedEncodingException {
        final String methodName = "DownloadService#downloadFile";

        if (!Files.exists(filePath)) {
            String msg = "존재하지 않는 파일입니다.";
            log.error("[{}] {} [path: {}]", methodName, msg, filePath);
            throw new FileNotFoundException(msg);
        } else if (Files.isDirectory(filePath)) {
            String msg = "디렉토리는 다운로드 할 수 없습니다.";
            log.error("[{}] {} [path: {}]", methodName, msg, filePath);
            throw new FileNotFoundException(msg);
        } else {
            String contentDisposition = new StringBuffer("attachment; filename=\"") //
                    .append(URLEncoder.encode(downloadFileName, "UTF-8").replace("+", "%20")) //
                    .append("\"") //
                    .toString();

            log.debug("[{}] 다운로드 할 파일 이름: {}", methodName, downloadFileName);

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentLength((int) filePath.toFile().length());
            response.setHeader("Content-Disposition", contentDisposition);

            try {
                Files.copy(filePath, response.getOutputStream());
                log.debug("[{}] 파일 다운로드 요청 성공 [file name: {}]", methodName, downloadFileName);
            } catch (IOException ioe) {
                log.error("[{}] 파일 다운로드 실패 [msg: {}]", methodName, ioe.getMessage());
                ioe.printStackTrace();
            }
        }
    }

    /**
     * plain text를 파일 형태로 다운로드
     * 
     * @param downloadFileName
     *            다운로드할 파일 이름
     * @param contents
     *            다운로드할 파일의 내용
     * @param response
     *            {@link HttpServletResponse}
     * 
     * @throws UnsupportedEncodingException
     *             파일 이름 인코딩 설정이 잘못 되었을 경우. 발생하지 않을 듯...
     * 
     * @author MJ Youn
     * @since 2022. 11. 24.
     */
    public void downloadPlainTextFile(@NotBlank String downloadFileName, @NotNull String contents, @NotNull HttpServletResponse response) throws UnsupportedEncodingException {
        final String methodName = "DownloadService#downloadPlainTextFile";

        String contentDisposition = new StringBuffer("attachment; filename=\"") //
                .append(URLEncoder.encode(downloadFileName, "UTF-8").replace("+", "%20")) //
                .append("\"") //
                .toString();

        log.debug("[{}] 다운로드 할 파일 이름: {}", methodName, downloadFileName);

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentLength((int) contents.length());
        response.setHeader("Content-Disposition", contentDisposition);

        try {
            InputStream inputStream = new ByteArrayInputStream(contents.getBytes());

            FileCopyUtils.copy(inputStream, response.getOutputStream());
            log.debug("[{}] 파일 다운로드 요청 성공 [file name: {}]", methodName, downloadFileName);
        } catch (IOException ioe) {
            log.error("[{}] 파일 다운로드 실패 [msg: {}]", methodName, ioe.getMessage());
            ioe.printStackTrace();
        }
    }

}
