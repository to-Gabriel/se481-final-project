

pipeline {
    agent any
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
                githubRelease(
                    credentialId: 'github-pat',

                    user:"${GITHUB_USER}",
                    repo: "${GITHUB_REPO}",

                    tagName: "${RELEASE_TAG}",
                    releaseName: "Release ${RELEASE_TAG}",
                    releaseNotes: "Automated release of build #${BUILD_NUMBER}",

                    artifactPatterns: 'target/*.jar'
                )
            }
        }

    }
}
