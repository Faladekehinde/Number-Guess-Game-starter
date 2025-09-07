# ---------- Build stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -ntp clean package

# ---------- Runtime stage ----------
# Use Tomcat 9 (Servlet 4, javax.*) to match our code
FROM tomcat:9.0-jdk17-temurin
# Remove default apps to keep image small & clean
RUN rm -rf /usr/local/tomcat/webapps/*
# Copy our built WAR as ROOT.war so it serves at '/'
COPY --from=build /app/target/NumberGuessGame.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
