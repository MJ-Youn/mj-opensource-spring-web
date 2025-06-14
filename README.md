# mj-opensource-spring-web
 - Spring으로 개발하는 웹 서비스 개발을 진행하면서 필요한 유틸성 기능을 추가 개발하는 유틸용 library

# How To Use


## release note
### 0.1.0-SNAPSHOT - 20220104
 - logging을 위한 Aspect 추가
 - http 연결 thread의 이름 변경을 위한 filter 추가
 - marker class 추가 

### 0.1.1-SNAPSHOT - 20220104
- ErrorController 추가 

### 0.1.1-SNAPSHOT - 20220112
- ErrorController 수정
    + 로그 추가

### 0.1.1 - 20220117
- 배포를 위한 버전 변경 

### 0.1.2 - 20220223
- spring web security 내용 추가
- security에서 자주 사용할만한 내용 추가 

### 0.1.3, 0.1.4 - 20220315
- security log 출력 오류 수정 

### 0.1.5, 0.1.6 - 20220315
- AbstractCustomFilterInvocationSecurityMetadataSource.java의 convert(List<AbstractGrade>) 함수 추가

### 0.1.7 - 20220401
- AbstractCustomFilterInvocationSecurityMetadataSource.java의 context path 출력 오류 수정 

### 0.1.8 ~ 0.2.0 - 20221124
- DownloadService 추가
 + 파일 다운로드 관련 기능
- SecurityUtils 추가
 + spring security에서 사용할 기능

### 0.2.1 - 20221213
- DownloadService 오류 수정

### 0.2.2 - 20231116
- DownloadService의 downloadBytes 함수 추가

### 0.2.3 - 20240207
- DownloadService에 Excel 관련 기능 추가

### 0.2.4 - 20240219
- Security 오류 수정

### 0.2.5 - 20240509
- StopWatch Aspect 추가

### 0.2.6 - 20240509
- annotation 조회 오류 수정

### 0.2.7 - 20240514
- CSVService에 직접 저장 기능 추가

### 0.2.8 - 20240522
- 사용하지 않는 dependency 제거

### 0.2.9, 0.2.10, 0.3.0 - 20240620
- HttpRequestLog의 순서 설정
- GetMapping, PostMapping, PatchMapping, PutMapping, DeleteMapping도 aspect로 로그를 찍도록 수정

### 0.3.1 - 20240628
- StopWatch Annotation 테스트 완료

### 0.3.2 - 20250218
 - 배포 library 변경 
  + nexus-staging-maven-plugin -> central-publishing-maven-plugin
  + repository 위치 변경 (ossrh -> central)
  + license, developer info 추가

### 0.3.3 - 20250612
 - mj-opensource-core의 버전 변경 (0.2.0 -> 0.2.2)