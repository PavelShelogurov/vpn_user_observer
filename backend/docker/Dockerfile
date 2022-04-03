FROM openjdk:11

ENV PORT=8080
ENV APP_WORKDIR=/usr/src/backend/
ENV PROPERTY_PATH=/resources/

COPY target/health-checker-1.jar ${APP_WORKDIR}
WORKDIR ${APP_WORKDIR}
RUN mkdir ${PROPERTY_PATH}

ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-Dspring.config.location=${APP_WORKDIR}${PROPERTY_PATH}", "-jar", "health-checker-1.jar"]
