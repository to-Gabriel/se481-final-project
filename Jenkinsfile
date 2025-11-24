

pipeline {
    agent any

    environment {
        GITHUB_REPO = 'to-Gabriel/se481-final-project'
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
                createGitHubRelease(
                    bodyText: "Automated release of build #${env.BUILD_NUMBER}",
                    credentialId: 'github-pat',
                    draft: false,
                    name: "${env.RELEASE_TAG}",
                    prerelease: false,
                    repository: "${GITHUB_REPO}",
                    tag: "${env.RELEASE_TAG}",
                    commitish: "${env.GIT_COMMIT}"
                )
            }
        }

    }
}
