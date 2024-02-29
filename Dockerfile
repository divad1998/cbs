FROM gradle:8.6.0-jdk21-jammy AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . /home/gradle/src
RUN gradle build --no-daemon

# Now, we setup the runtime environment.
# Use OpenJDK for running the application.
FROM eclipse-temurin:21-jdk-jammy

# Copy the built artifact from the build stage.
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]