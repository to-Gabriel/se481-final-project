

pipeline {
    agent any

    environment {
        GITHUB_USER = 'to-Gabriel'
        GITHUB_REPO = 'se481-final-project'

        RELEASE_TAG = "v1.0-b${BUILD_NUMBER}"
    }

    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Deliver (Release to Github)') { 
            steps {
                createGitHubRelease credentialId: 'github-pat', draft: false, prerelease: false, repository: 'https://github.com/to-Gabriel/se481-final-project.git'
            }
        }

    }
}
