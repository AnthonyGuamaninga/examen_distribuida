# docker build -t app-books .

# docker login
# docker build -t aaguamaninga/app-books-examen .
# docker push aaguamaninga/app-books-examen

FROM eclipse-temurin:21.0.5_11-jre-alpine

RUN mkdir /app
WORKDIR /app

COPY build/quarkus-app/app ./app
COPY build/quarkus-app/lib ./lib
COPY build/quarkus-app/quarkus ./quarkus
COPY build/quarkus-app/quarkus-run.jar app.jar

CMD ["java", "-jar", "app.jar"]