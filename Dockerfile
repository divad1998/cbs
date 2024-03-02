# Build stage
# FROM eclipse-temurin:21-jdk-jammy AS build
# WORKDIR /app
# COPY . /app
# RUN chmod +x ./gradlew
# RUN ./gradlew build --no-daemon
#
# # Run stage
# FROM eclipse-temurin:21-jdk-jammy
# WORKDIR /app
# # Copy the built JAR file from the previous stage
# COPY --from=build /app/build/libs/*.jar app.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "app.jar"]

#use this
FROM eclipse-temurin:21-jdk-jammy
VOLUME /tmp
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]