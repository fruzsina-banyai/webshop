# Use an official OpenJDK runtime as a parent image
FROM amazoncorretto:17

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY ./build /app

# Set the application port
ENV PORT=8080
ENV GL_UMS_DB_HOST=localhost
ENV GL_UMS_DB_NAME=user_management
ENV GL_UMS_DB_PASSWORD=postgres
ENV GL_UMS_DB_PORT=5432
ENV GL_UMS_DB_USER_NAME=postgres

# Expose the application port
EXPOSE $PORT

# Start the application
CMD ["java", "-jar", "/app/libs/application.jar"]