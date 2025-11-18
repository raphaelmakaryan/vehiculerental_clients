FROM openjdk:26-ea-slim-trixie
WORKDIR /app
COPY ./target/vehicleRentalClients-0.0.1-SNAPSHOT.jar /app/vehicleRentalClients-0.0.1-SNAPSHOT.jar
EXPOSE 8081
CMD ["java", "-jar", "/app/vehicleRentalClients-0.0.1-SNAPSHOT.jar"]