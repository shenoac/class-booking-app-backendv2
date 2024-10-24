FROM registry.access.redhat.com/ubi8/ubi-minimal:8.7

WORKDIR /work/
COPY ./target/class-booking-app-backend-1.0.0-SNAPSHOT-runner /work/application

# Expose the correct port
EXPOSE 8080

# Use a non-root user for better security
RUN chmod 775 /work /work/application && chown -R 1001 /work
USER 1001

CMD ["./application", "-Dquarkus.http.host=0.0.0.0", "-Dquarkus.http.port=$PORT"]
