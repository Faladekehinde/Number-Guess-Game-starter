pipeline {
  agent any

  tools {
    jdk 'JDK17'          // Manage Jenkins > Global Tool Configuration
    maven 'Maven3'       // Name your Maven installation 'Maven3'
  }

  environment {
    DOCKER_IMAGE = "faladekehinde/number-guess-game"
    DOCKERHUB = credentials('dockerhub-creds') // add in Jenkins Credentials
    SONARQUBE_TOKEN = credentials('sonar-token') // SonarQube token ID
  }

  options {
    timestamps()
    buildDiscarder(logRotator(numToKeepStr: '10'))
  }

  triggers {
    pollSCM('H/5 * * * *') // fallback, use GitHub webhook if possible
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn -B -ntp clean verify jacoco:report'
      }
      post {
        always {
          junit 'target/surefire-reports/*.xml'
          archiveArtifacts artifacts: 'target/*.war', fingerprint: true
        }
      }
    }

    stage('Code Coverage') {
      steps {
        recordCoverage(
          tools: [jacoco(pattern: 'target/site/jacoco/jacoco.xml')],
          sourceCodeRetention: 'EVERY_BUILD'
        )
      }
    }

    stage('SonarQube Analysis') {
      steps {
        withSonarQubeEnv('MySonarQube') { // match name in Jenkins SonarQube config
          sh """
            mvn sonar:sonar \
              -Dsonar.projectKey=NumberGuessGame \
              -Dsonar.host.url=http://localhost:9000 \
              -Dsonar.login=${SONARQUBE_TOKEN} \
              -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
          """
        }
      }
    }

    stage('Docker Build') {
      steps {
        sh 'docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} -t ${DOCKER_IMAGE}:latest .'
      }
    }

    stage('Docker Push') {
      steps {
        sh 'echo "${DOCKERHUB_PSW}" | docker login -u "${DOCKERHUB_USR}" --password-stdin'
        sh 'docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}'
        sh 'docker push ${DOCKER_IMAGE}:latest'
      }
    }

    stage('Deploy (Local Docker)') {
      steps {
        sh '''
          docker rm -f number-guess-game || true
          docker pull ${DOCKER_IMAGE}:latest
          docker run -d --name number-guess-game -p 9090:8080 ${DOCKER_IMAGE}:latest
        '''
      }
    }
  }

  post {
    success {
      echo "✅ Deployed! Open http://<your-server-ip>:9090/NumberGuessGame"
    }
    failure {
      echo "❌ Build failed. Check the logs above."
    }
  }
}