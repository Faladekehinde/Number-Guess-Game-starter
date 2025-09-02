pipeline {
  agent any

  tools {
    jdk 'JDK17'          // Manage Jenkins > Global Tool Configuration
    maven 'Maven3'       // Name your Maven installation 'Maven3'
  }

  environment {
    DOCKER_IMAGE = "yourdockerhub/number-guess-game"
    DOCKERHUB = credentials('dockerhub-creds') // add in Jenkins Credentials
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
          docker run -d --name number-guess-game -p 8080:8080 ${DOCKER_IMAGE}:latest
        '''
      }
    }
  }

  post {
    success {
      echo "Deployed! Open http://<your-server-ip>:8080/"
    }
    failure {
      echo "Build failed. Check the logs above."
    }
  }
}
