FROM eclipse-temurin:26-jdk AS build
WORKDIR /app

COPY gradlew build.gradle.kts settings.gradle.kts ./
COPY gradle gradle

RUN ./gradlew dependencies --no-daemon

COPY src ./src

RUN ./gradlew installDist --no-daemon

FROM eclipse-temurin:26-jre
WORKDIR /app

COPY --from=build /app/build/install/BunkerKT /app/

EXPOSE 8080

CMD ["./bin/BunkerKT"]