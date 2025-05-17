# Dockerfile for building and running the application container
FROM openjdk:21-jdk-slim

# Set environment variables
ENV LANGUAGE='en_US:en'

# Create app directory
WORKDIR /app

# Copy the application files into the container
COPY start.sh /app/start.sh
COPY . /app

# Make the start.sh script executable
RUN chmod +x /app/start.sh

# Expose the application port
EXPOSE 8080

# Set the default command to execute the start.sh script
CMD ["/app/start.sh"]