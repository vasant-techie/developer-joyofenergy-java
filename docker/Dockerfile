####
# This Dockerfile is used in order to build a container that runs the Spring Boot application
#
# Build the image with:
#
# docker build -f docker/Dockerfile -t springboot/sample-demo .
#
# Then run the container using:
#
# docker run -i --rm -p 8081:8081 springboot/sample-demo
####
FROM maven:3.8.1-openjdk-17-slim

WORKDIR /build

# Build dependency offline to streamline build
COPY build.gralde .
RUN gradlew --offline

COPY src src
RUN gradlew build

FROM public.ecr.aws/docker/library/openjdk:11
COPY --from=0 /build/build/libs/joi-energy.jar /app/target/joi-energy.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/target/calc-rest-api-1.0.jar", "--server.port=8080" ]
