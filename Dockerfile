# Use the openjdk image as the base image
FROM openjdk:17-slim AS build

# Install Maven 3.8.7 manually
ENV MAVEN_VERSION=3.8.7
ENV MAVEN_HOME=/opt/maven
ENV PATH=${MAVEN_HOME}/bin:${PATH}

# Install Maven dependencies and tools
RUN apt-get update && \
    apt-get install -y curl tar && \
    curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | \
    tar xz -C /opt && \
    mv /opt/apache-maven-${MAVEN_VERSION} ${MAVEN_HOME} && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Set the environment variable for binding to 0.0.0.0 in Docker
ENV QUARKUS_HTTP_HOST=0.0.0.0

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy the entire source code, including application.properties
COPY src ./src
COPY src/main/resources/application.properties ./src/main/resources/application.properties

# Expose the Quarkus default port
EXPOSE 8080

# Run Quarkus in dev mode
CMD ["mvn", "quarkus:dev"]
