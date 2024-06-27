# 기본 이미지 설정
FROM openjdk:17

# 이 Dockerfile 내부에서만 사용될 변수 설정
ARG JAR_FILE=build/libs/*.jar

# JAR_FILE 변수를 사용하여 컨테이너에 'app.jar' 의 이름으로 파일 복사
COPY ${JAR_FILE} app.jar

# 8080 포트 노출
EXPOSE 8080

# 컨테이너가 시작될 때 실행되는 명령어 지정 (스크립트 파일 지정도 가능)
ENTRYPOINT ["java", "-jar", "/app.jar"]