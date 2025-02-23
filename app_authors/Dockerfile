# docker build -t app-authors .
# docker build -t aaguamaninga/app-authors-examen .
# docker push aaguamaninga/app-authors-examen

FROM eclipse-temurin:21.0.5_11-jre-alpine

RUN mkdir -p /app/resources/db/migration
WORKDIR /app

COPY build/classes/java/main ./main
COPY build/libs/app_authors-all.jar app.jar


# migración de Flyway
COPY src/main/resources/db/migration/ /app/resources/db/migration/

CMD ["java", "-jar", "/app/app.jar"]
