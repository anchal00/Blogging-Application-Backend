FROM openjdk:11
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY target/BloggingApplication-0.0.1-SNAPSHOT.jar bloggingapplication.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "bloggingapplication.jar"]
