# transaction-exercise

This exercise consists of a small service using a very small CPU and memory footprint to store transactions with timestamps within the last 60 seconds.

# Requirements
- Java 11 (`sdk use java 11.0.4.hs-adpt`)
- Maven
- Docker

# Build the app
`mvn clean install`

# Start a container + build a docker image
`docker-compose up --build`

# List of API endpoints
http://localhost:8080/swagger-ui.html