

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

                uploadGithubReleaseAsset(
                    credentialId: 'github-pat',
                    repository: "${env.GITHUB_REPO}",
                    tagName: "${env.RELEASE_TAG}", 
                    uploadAssets: [
                        [
                            filePath: 'target/Asteroids.jar',
                            contentType: 'application/java-archive'
                        ]
                    ]
                )
            }
        }

    }

    // Runs after all stages complete
    post {
        always {
            emailext(
                subject: "Jenkins Build ${currentBuild.currentResult}: ${env.JOB_NAME} - #${env.BUILD_NUMBER}",
                body: """
                    <p><b>Build Status:</b> ${currentBuild.currentResult}</p>
                    <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                    <p><b>Project:</b> ${env.JOB_NAME}</p>
                    <p><b>Check console output at:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                """,

                recipientProvider: [requestor(), developers()],
                attachLog: true,
                compressLog: true
            )
        }
    }
}
