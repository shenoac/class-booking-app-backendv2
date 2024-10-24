# Stage 1: Build the Native Image using GraalVM
FROM quay.io/quarkus/ubi-quarkus-native-image:22.3-java17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy your app source and build it into a native binary
COPY . .

# Ensure proper file permissions
RUN chmod -R 775 .

# Build the native executable using GraalVM
RUN ./mvnw package -Pnative -Dquarkus.native.container-build=true

# Stage 2: Minimal image to run the native app
FROM registry.access.redhat.com/ubi8/ubi-minimal:8.7

WORKDIR /work/
COPY --from=build /app/target/*-runner /work/application

# Expose the port for the app
EXPOSE 8080

# Use a non-root user for better security
RUN chmod 775 /work /work/application && chown -R 1001 /work
USER 1001

# Run the native executable with the correct host binding
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
