# Stage 1: Build the Native Image
FROM quay.io/quarkus/ubi-quarkus-native-image:22.3-java17 AS build

WORKDIR /app

# Copy your app source and build it into a native binary
COPY . .
RUN ./mvnw package -Pnative -Dquarkus.native.container-build=true

# Stage 2: Minimal image to run the native app
FROM registry.access.redhat.com/ubi8/ubi-minimal:8.7

WORKDIR /work/
COPY --from=build /app/target/*-runner /work/application

# Expose the port (Heroku dynamically assigns the port, bind to 0.0.0.0)
EXPOSE 8080
CMD ["./application", "-Dquarkus.http.host=0.0.0.0", "-Dquarkus.http.port=${PORT}"]
