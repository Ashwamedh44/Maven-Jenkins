pipeline {
    agent any
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
                    bat 'for /r src/main/java/com/mycompany/app %%f in (*.java) do javac %%f'
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
                    -Dsonar.organization=Jenkins_Ashwamedh\
                    -Dsonar.login=5a34cfc296f871ba413ae50c2a5a596d73f3d5ee
                """
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
        always {
            script {
                emailext (
                    body: """
                    Hey, The build was ${currentBuild.currentResult}:!!!
                    Job '${env.JOB_NAME}' (${env.BUILD_NUMBER})
                    Build URL: ${env.BUILD_URL}
                    """,
                    subject: "Build ${currentBuild.currentResult}: Job '${env.JOB_NAME}' (${env.BUILD_NUMBER})",
                    to: 'ashwamedh.arote25@gmail.com',
                    attachLog: true
                )
            }
        }
    }
}
