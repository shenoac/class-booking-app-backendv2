# Use a base image with OpenJDK
FROM openjdk:21-slim

# Set the working directory in the container
WORKDIR /app

# Copy all necessary files from the target/quarkus-app folder into the container
COPY target/quarkus-app/quarkus-run.jar /app/quarkus-run.jar
COPY target/quarkus-app/lib/ /app/lib/
COPY target/quarkus-app/app/ /app/app/
COPY target/quarkus-app/quarkus/ /app/quarkus/

# Expose the port the app runs on (default is 8080)
EXPOSE 8080

# Run the Quarkus JAR file
CMD ["java", "-jar", "/app/quarkus-run.jar"]
