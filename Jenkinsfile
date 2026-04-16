pipeline {
    agent any

    environment {
        // --- EDIT THESE PATHS TO MATCH THE PC YOU ARE ON ---
        TOMCAT_PATH = 'E:/apache-tomcat-10.1.28'
        // Use the full path to Maven if it's not in your Windows PATH
        MAVEN_HOME  = 'C:/Users/heg/.m2/wrapper/dists/apache-maven-3.9.12/59fe215c0ad6947fea90184bf7add084544567b927287592651fda3782e0e798/bin'
        JAVA_HOME   = 'C:/Program Files/Java/jdk-17'
    }

    stages {
        stage('Stop Existing Server') {
            steps {
                echo "Finding and killing process on port 8090..."
                // The '|| exit 0' ensures the build continues even if no process is found
                bat 'for /f "tokens=5" %%a in (\'netstat -aon ^| findstr :8090\') do taskkill /f /pid %%a 2>nul || exit 0'
            }
        }

        stage('Clean and Build') {
            steps {
                echo "Building fresh WAR file with Maven..."
                // Use the explicit path to Maven to avoid 'command not found' errors
                bat "\"${MAVEN_HOME}/mvn.cmd\" clean package -DskipTests"
            }
        }

        stage('Surgical Clean and Deploy') {
            steps {
                echo "Cleaning old deployment folder and copying new WAR..."
                // Step 1: Remove the old unzipped folder to prevent caching old data
                bat "if exist \"${TOMCAT_PATH}/webapps/demo\" rd /s /q \"${TOMCAT_PATH}/webapps/demo\""
                
                // Step 2: Copy the fresh .war from the current workspace
                // In Pipeline, the WAR is in the current workspace 'target' folder
                bat "xcopy /y \"target\\demo.war\" \"${TOMCAT_PATH}/webapps/\""
            }
        }

        stage('Start Tomcat Server') {
            steps {
                echo "Starting Tomcat in a detached process..."
                // Use 'start /B' and set BUILD_ID to ensure the server stays alive
                withEnv(['BUILD_ID=dontKillMe']) {
                    bat """
                        set "JAVA_HOME=${JAVA_HOME}"
                        cd /d "${TOMCAT_PATH}/bin"
                        start /B startup.bat
                    """
                }
            }
        }
    }

    post {
        success {
            echo "------------------------------------------------------------"
            echo "DEPLOYMENT SUCCESSFUL!"
            echo "URL: http://localhost:8090/demo/users"
            echo "------------------------------------------------------------"
        }
        failure {
            echo "BUILD FAILED. Check the console output for specific errors."
        }
    }
}