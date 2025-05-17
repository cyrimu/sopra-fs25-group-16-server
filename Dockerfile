FROM gradle:7.6-jdk17 as build
# Set container working directory to /app
WORKDIR /app
# Copy Gradle configuration files
COPY gradlew gradlew.bat /app/
COPY gradle /app/gradle
# Ensure Gradle wrapper is executable
RUN chmod +x ./gradlew
# Copy build script and source code
COPY build.gradle settings.gradle /app/
COPY src /app/src
# Build the server
RUN ./gradlew clean build --no-daemon --stacktrace --info

# make image smaller by using multi-stage build
FROM openjdk:17-slim
# Set the env to "production"
ENV SPRING_PROFILES_ACTIVE=production
# get non-root user
USER 3301
# Set container working directory to /app
WORKDIR /app
# Create a directory for jar files
RUN mkdir -p /app/jars
# copy built artifact from build stage to a directory
COPY --from=build /app/build/libs/*.jar /app/jars/
# Expose the port on which the server will be running (based on application.properties)
EXPOSE 8080
# start server - adjusting the CMD to point to the new jar path
CMD ["java", "-jar", "/app/jars/soprafs24.jar"]
