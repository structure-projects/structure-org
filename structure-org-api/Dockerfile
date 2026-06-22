FROM eclipse-temurin:17-jdk-alpine
ADD ./target/org-server.jar /app/boot/app.jar
ADD liveness.sh /app/liveness.sh
ENV PARAMS=""
ENV JAVA_OPTS=""
ENV APP_PATH="/app/boot/app.jar"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS $PARAMS -jar $APP_PATH","-ea","&"]
EXPOSE 8080
