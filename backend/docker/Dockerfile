FROM openjdk:11
ENV PORT=8081
COPY target/health-checker-1.jar /usr/src/backend/
WORKDIR /usr/src/backend/
ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-jar", "health-checker-1.jar"]
