#
# Build stage
#
FROM maven:3.8.2-jdk-8-slim
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip=true

#
# Package stage
#
FROM openjdk:8-jre-slim
COPY --from=0 /home/app/target/sw-debt-server.jar /usr/local/lib/app.jar
EXPOSE 11080
CMD java ${JAVA_OPTS} -Dspring.profiles.active=${ACTIVE_PROFILES} -jar /usr/local/lib/app.jar
