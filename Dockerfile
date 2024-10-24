# Stage 1: Minimal image to run the pre-built native app
FROM registry.access.redhat.com/ubi8/ubi-minimal:8.7

WORKDIR /work/
COPY ./target/*-runner /work/application

# Expose the port for the app
EXPOSE 8080

# Use a non-root user for better security
RUN chmod 775 /work /work/application && chown -R 1001 /work
USER 1001

# Run the native executable with the correct host binding
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
