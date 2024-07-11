# 기본 이미지 설정
FROM openjdk:17-jdk-slim

# 빌드 도구 설치
RUN apt-get update && apt-get install -y curl unzip zip

# SDKMAN을 이용해 Gradle 설치
RUN curl -s https://get.sdkman.io | bash && \
    bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install gradle 7.3"

# 소스 코드를 컨테이너에 복사
COPY . /app

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 캐시 무효화를 위해 환경 변수 설정
ARG CACHEBUST=1

# Gradle 빌드를 실행하여 JAR 파일 생성
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && gradle clean build bootJar --no-daemon"

# JAR 파일이 생성되었는지 확인
RUN ls -l /app/build/libs/

# JAR 파일을 컨테이너로 복사합니다.
COPY /app/build/libs/*.jar app.jar

# 애플리케이션이 사용하는 포트를 노출합니다.
EXPOSE 8080

# 애플리케이션을 실행합니다.
ENTRYPOINT ["java", "-jar", "/app.jar"]
