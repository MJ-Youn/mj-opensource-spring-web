package io.github.mjyoun.spring.web.service;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.NumberFormat;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;

import lombok.extern.slf4j.Slf4j;

/**
 * CSV 파일 관련된 서비스
 * 
 * @author MJ Youn
 * @since 2022. 06. 21.
 */
@Slf4j
@Service(CSVService.QUALIFIER_NAME)
public class CSVService {

    public static final String QUALIFIER_NAME = "io.github.mjyoun.spring.web.service.CSVService";

    /**
     * CSV 파일 정보를 생성하는 함수
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
     * @return CSV 파일 데이터가 들어있는 byte array
     * 
     * @throws IOException
     *             CSV FileWriter 생성 실패
     * 
     * @author MJ Youn
     * @since 2022. 06. 21.
     */
    public byte[] createCSV(@NotNull String fileName, String[] headers, List<String[]> datas, char separator, char quote) throws IOException {
        final String methodName = "CSVService#createCSV";

        // 따옴표가 설정되어 있는지 여부
        boolean wasSetQuote = quote != '\0';

        StringWriter writer = new StringWriter();
        // bom encoding 추가
        writer.append("\ufeff");
        try (CSVWriter csvWriter = new CSVWriter(writer, //
                separator == '\0' ? CSVWriter.DEFAULT_SEPARATOR : separator, // 구분자
                quote == '\0' ? CSVWriter.DEFAULT_QUOTE_CHARACTER : quote, // quote
                CSVWriter.DEFAULT_ESCAPE_CHARACTER, // 예외 처리 문자
                CSVWriter.DEFAULT_LINE_END // 라인 끝 문자
        )) {
            // header 입력
            if (headers == null) {
                log.debug("[{}] 헤더가 없는 CSV 파일", methodName);
            } else {
                csvWriter.writeNext(headers);
                log.debug("[{}] 헤더 설정 완료 [header count: {}]", methodName, headers.length);
            }

            // body 입력
            if (wasSetQuote) {
                log.debug("[{}] 따옴표가 설정되어 있어, 모든 데이터를 따옴표로 감싸서 출력합니다. [quote: {}]", methodName, quote);
            }

            csvWriter.writeAll(datas, wasSetQuote);
            log.debug("[{}] 데이터 설정 완료 [data count: {}]", methodName, NumberFormat.getInstance().format(datas.size()));
        }

        String fileContent = writer.toString();

        return fileContent.getBytes();
    }

    /**
     * CSV 파일 저장
     * 
     * @param path
     *            파일 저장 위치
     * @param headers
     *            헤더 목록. null일 경우 헤더가 없는 형태의 csv 파일
     * @param datas
     *            데이터 목록
     * @param separator
     *            구분자
     * @param quote
     *            따옴표, 없을 경우 따옴표 하지 않음. 있을 경우 무조건 따옴표로 묶음
     * @throws IOException
     *             CSV FileWriter 생성 실패
     * 
     * @author MJ Youn
     * @since 2024. 05. 14.
     */
    public void saveCSV(@NotNull Path path, String[] headers, List<String[]> datas, char separator, char quote) throws IOException {
        final String methodName = "CSVService#saveCSV";
        byte[] csvBytes = this.createCSV("test", headers, datas, separator, quote);

        Path directory = path.getParent();
        Files.createDirectories(directory);
        log.debug("[{}] 상위 디렉토리 생성 [directory: {}]", methodName, directory.normalize().toString());

        Files.write(path, csvBytes, StandardOpenOption.CREATE);
        log.debug("[{}] 파일 저장 완료 [path: {}]", methodName, path.normalize().toString());
    }

}
