FROM openjdk:17-slim AS build

ENV MAVEN_VERSION=3.8.7
ENV MAVEN_HOME=/opt/maven
ENV PATH=${MAVEN_HOME}/bin:${PATH}

RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar xz -C /opt && \
    mv /opt/apache-maven-${MAVEN_VERSION} ${MAVEN_HOME} && \
    apt-get clean


WORKDIR /app


COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/main/java ./src/main/java
EXPOSE 8080
CMD ["mvn", "quarkus:dev"]
