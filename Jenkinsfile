pipeline {
  agent any

  tools {
    jdk 'JDK17'          // Manage Jenkins > Global Tool Configuration
    maven 'Maven3'       // Name your Maven installation 'Maven3'
  }

  environment {
    DOCKER_IMAGE = "faladekehinde/number-guess-game"
    SONARQUBE_AUTH_TOKEN = credentials('sonar-token')   // <-- your SonarQube token
  }

  options {
    timestamps()
    buildDiscarder(logRotator(numToKeepStr: '10'))
  }

  triggers {
    // Use a GitHub webhook if possible; this is a fallback
    pollSCM('H/5 * * * *')
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn -B -ntp clean verify'
      }
      post {
        always {
          junit 'target/surefire-reports/*.xml'
          archiveArtifacts artifacts: 'target/*.war', fingerprint: true
        }
      }
    }

    stage('SonarQube Analysis') {
      steps {
        withSonarQubeEnv('MySonarQube') {  // Name you set in SonarQube config
          sh """
            mvn sonar:sonar \
              -Dsonar.projectKey=NumberGuessGame \
              -Dsonar.host.url=http://localhost:9000 \
              -Dsonar.token=${SONARQUBE_AUTH_TOKEN}
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
        withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
          sh 'docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}'
          sh 'docker push ${DOCKER_IMAGE}:latest'
        }
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
      echo "Deployed! Open http://<your-server-ip>:9090/"
    }
    failure {
      echo "Build failed. Check the logs above."
    }
  }
}
