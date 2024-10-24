# Stage 1: Build the Native Image using GraalVM
FROM quay.io/quarkus/ubi-quarkus-native-image:22.3-java17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY pom.xml ./

# Download Maven dependencies
RUN ./mvnw dependency:go-offline

# Copy the entire source code
COPY src ./src

# Build the native executable using GraalVM
RUN ./mvnw package -Pnative -Dquarkus.native.container-build=true

# Stage 2: Create the minimal image to run the native executable
FROM registry.access.redhat.com/ubi8/ubi-minimal:8.7

WORKDIR /work/
COPY --from=build /app/target/*-runner /work/application

# Expose the port for the app
EXPOSE 8080

# Use a non-root user for better security
RUN chmod 775 /work/application && chown -R 1001 /work
USER 1001

# Run the native executable with the correct host binding
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
