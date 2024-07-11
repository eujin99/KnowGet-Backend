FROM openjdk:17

# 빌드 과정에서 생성된 JAR 파일의 경로를 명시합니다.
ARG JAR_FILE=build/libs/app.jar

# JAR 파일을 컨테이너로 복사합니다.
COPY ${JAR_FILE} app.jar

# 애플리케이션이 사용하는 포트를 노출합니다.
EXPOSE 8080

# 애플리케이션을 실행합니다.
ENTRYPOINT ["java", "-jar", "/app.jar"]
