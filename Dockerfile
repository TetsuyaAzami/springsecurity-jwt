FROM amazoncorretto:17-alpine-jdk as development

WORKDIR /app

COPY ./gradle/ ./gradle/
COPY ./gradlew ./settings.gradle ./build.gradle ./
COPY ./src/ ./src/

RUN ./gradlew build

EXPOSE 8080
EXPOSE 8000

ENTRYPOINT [ "./gradlew", "bootRun", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8080"]
