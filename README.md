# NumberGuessGame — CI/CD + Docker (Kid-friendly)

A tiny Java **Number Guessing Game** (Servlet + JSP) with a full CI/CD pipeline and Docker.

## How to run on your computer

1. Install **Java 17** and **Maven** and **Docker**.
2. In a terminal:
   ```bash
   mvn clean package
   docker build -t number-guess-game:local .
   docker run -p 8080:8080 number-guess-game:local
   ```
3. Open: <http://localhost:8080/>

## Files you care about

```
NumberGuessGame/
├─ src/main/java/com/studentapp/NumberGuessServlet.java
├─ src/main/java/com/studentapp/NumberUtils.java
├─ src/main/webapp/index.jsp
├─ src/main/webapp/WEB-INF/web.xml
├─ src/test/java/com/studentapp/NumberUtilsTest.java
├─ pom.xml
├─ Dockerfile
├─ docker-compose.yml
├─ Jenkinsfile
└─ README.md
```

## Jenkins (CI/CD)

1. Create a **new Pipeline** job.
2. Choose **Pipeline script from SCM** and point to your Git repo containing this project.
3. Make sure Jenkins has tools installed (Manage Jenkins → Global Tool Configuration):
   - JDK named `JDK17`
   - Maven named `Maven3`
4. Add Credentials:
   - **dockerhub-creds** (Username + Password) for Docker Hub.
5. Run a build. On success, Jenkins will:
   - Build & test with Maven
   - Build and push a Docker image
   - Run the container locally on the Jenkins server (port 8080).

## Webhook trigger (event‑driven)

- In GitHub: **Settings → Webhooks → Add Webhook**  
  URL: `http://<your-jenkins>/github-webhook/`  
  Content type: `application/json`  
  Events: **Just the push event**

## Quality & reliability

- **JUnit 5** tests live in `src/test/java` (we test logic in `NumberUtils`).  
- **Checkstyle** runs during `mvn verify` to remind you about clean code (does **not** fail the build).  
- Docker image is based on **Tomcat 9 + Java 17** and serves the app at `/`.

Enjoy! 🎉
