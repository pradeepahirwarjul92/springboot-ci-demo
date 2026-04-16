pipeline {
    agent any 

    environment {
        // Define paths here so they are easy to change for your Sir's PC
        TOMCAT_PATH = 'E:/apache-tomcat-10.1.28'
        PROJECT_PATH = 'E:/Docker-User-Service-Docker'
    }

    stages {
        stage('Clean and Build') {
            steps {
                bat 'for /f "tokens=5" %%a in (\'netstat -aon ^| findstr :8090\') do taskkill /f /pid %%a 2>nul'
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                // Remove old folder and copy new WAR
                bat "if exist \"${TOMCAT_PATH}/webapps/demo\" rd /s /q \"${TOMCAT_PATH}/webapps/demo\""
                bat "xcopy /y \"${PROJECT_PATH}/target/demo.war\" \"${TOMCAT_PATH}/webapps/\""
            }
        }

        stage('Start Server') {
            steps {
                script {
                    // This replaces BUILD_ID=dontKillMe in Pipeline mode
                    jenkins.util.Timer.get().schedule({
                        bat "cd /d \"${TOMCAT_PATH}/bin\" && set JAVA_HOME=C:/Program Files/Java/jdk-17 && start /B startup.bat"
                    }, 5, java.util.concurrent.TimeUnit.SECONDS)
                }
            }
        }
    }
    
    post {
        success {
            echo "--------------------------------------------------------"
            echo "DEPLOYMENT SUCCESSFUL! ACCESS AT: http://localhost:8090/demo/users"
            echo "--------------------------------------------------------"
        }
    }
}