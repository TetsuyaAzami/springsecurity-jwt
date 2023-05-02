FROM amazoncorretto:17-alpine-jdk as development

WORKDIR /app

RUN apk update && \
    apk add git

COPY ./gradle/ ./gradle/
COPY ./gradlew ./settings.gradle ./build.gradle ./
COPY ./src/ ./src/

RUN ./gradlew build

EXPOSE 8080
EXPOSE 8000

ENTRYPOINT [ "/bin/sh"]
