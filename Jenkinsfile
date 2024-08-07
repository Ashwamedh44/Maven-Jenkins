pipeline {
    agent any
    parameters {
        booleanParam(name: 'PROCEED_WITH_BUILD', defaultValue: true, description: 'Check to continue with the build')
    }
    // environment {
    //     SONAR_TOKEN = credentials('5a34cfc296f871ba413ae50c2a5a596d73f3d5ee') // Replace with your actual credentials ID
    // }
    stages {
        stage('Check Java Version') {
            steps {
                bat 'java --version'
                bat 'javac --version'
            }
        }
 
        stage('Check Maven Version') {
            steps {
                bat 'mvn --version'
            }
        }
        
        stage('Pre-Build') {
            steps {
                script {
                    bat 'for /r src/main/java/com/jenkins_example/app %%f in (*.java) do javac %%f'
                }
            }
        }
        
        stage('SonarCloud Analysis') {
           steps{
            script{
                // Run SonarCloud analysis
                def scannerHome = tool name: 'SonarQube', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
                    withSonarQubeEnv('SonarQube') {
                bat """
                    C:\\ProgramData\\Jenkins\\.jenkins\\tools\\hudson.plugins.sonar.SonarRunnerInstallation\\SonarQube\\bin\\sonar-scanner\
                    -Dsonar.sources=src\\main\\java\
                    -Dsonar.projectKey=jenkins-ashwamedh_jenkins-java\
                    -Dsonar.branch.name=master\
                    -Dsonar.organization=jenkins-ashwamedh\
                    -Dsonar.login=5a34cfc296f871ba413ae50c2a5a596d73f3d5ee 
                """
            }
            }
            }
        }
        stage('Quality Gate Check') {
            steps {
                script {
                    // Wait for SonarCloud quality gate status
                    timeout(time: 10, unit: 'MINUTES') {
                        def qualityGate = waitForQualityGate()
                        if (qualityGate.status != 'OK') {
                            error("Quality gate failed: ${qualityGate.status}. Aborting build.")
                        }
                    }
                }
            }
        }

        stage('User Confirmation') {
            steps {
                script {
                    // Prompt user for confirmation
                    def userInput = input(
                        id: 'ProceedWithBuild', // Unique identifier for the input
                        message: 'Do you want to continue with the build?',
                        parameters: [
                            [$class: 'BooleanParameterDefinition', name: 'Continue', defaultValue: true, description: 'Check to continue']
                        ]
                    )

                    // You can use the userInput variable if needed
                    if (!userInput) {
                        error("User chose not to continue the build.")
                    }
                }
            }
        }
        
        stage('Build') {
            steps {
                bat 'mvn -B -DskipTests clean package'
            }
        }
        
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
    }
    post {
    success {
        script {
            emailext (
                body: """
                Hey, The build was successful!
                Job '${env.JOB_NAME}' (${env.BUILD_NUMBER})
                Build URL: ${env.BUILD_URL}
                """,
                subject: "Build Success: Job '${env.JOB_NAME}' (${env.BUILD_NUMBER})",
                to: 'ashwamedh.arote25@gmail.com',
                attachLog: true
            )
        }
    }
    failure {
        script {
            emailext (
                body: """
                Hey, The build failed!
                Job '${env.JOB_NAME}' (${env.BUILD_NUMBER})
                Build URL: ${env.BUILD_URL}
                """,
                subject: "Build Failure: Job '${env.JOB_NAME}' (${env.BUILD_NUMBER})",
                to: 'ashwamedh.arote25@gmail.com',
                attachLog: true
            )
        }
    }
    
}

}
