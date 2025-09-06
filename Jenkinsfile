pipeline {
    agent any

    environment {
        SONARQUBE_TOKEN = credentials('SONARQUBE_TOKEN') // Replace with your actual Jenkins credential ID
        DOCKERHUB      = credentials('DOCKERHUB_USER')   // Docker Hub username
        DOCKERHUB_PSW  = credentials('DOCKERHUB_PSW')    // Docker Hub password
    }

    tools {
        maven 'Maven3'  // Make sure your Jenkins Maven tool name matches
        JDK 'Java17'    // Make sure your Jenkins JDK tool name matches
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify jacoco:report'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    jacoco execPattern: '**/target/jacoco.exec'
                }
            }
        }

        stage('Code Quality (SonarQube)') {
            steps {
                withSonarQubeEnv('SonarQubeServer') { // Replace with your SonarQube server name in Jenkins
                    sh 'mvn sonar:sonar -Dsonar.login=$SONARQUBE_TOKEN'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t $DOCKERHUB/number-guess-game:latest .'
            }
        }

        stage('Docker Push') {
            steps {
                withDockerRegistry([credentialsId: 'dockerhub-credentials', url: '']) {
                    sh 'docker push $DOCKERHUB/number-guess-game:latest'
                }
            }
        }

        stage('Deploy (Local Docker)') {
            steps {
                sh 'docker run -d -p 8080:8080 --name number-guess-game $DOCKERHUB/number-guess-game:latest'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline completed successfully!'
        }
        failure {
            echo '❌ Build failed. Check logs above.'
        }
    }
}