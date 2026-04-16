pipeline {
    agent any

    triggers {
        // Automatically checks for saves/pushes to GitHub every 1 minute
        pollSCM('* * * * *') 
    }

    environment {
        // --- Paths for Sir's Machine ---
        TOMCAT_PATH = 'E:\\apache-tomcat-10.1.28'
        MAVEN_HOME  = 'C:\\Users\\heg\\.m2\\wrapper\\dists\\apache-maven-3.9.12\\59fe215c0ad6947fea90184bf7add084544567b927287592651fda3782e0e798\\bin'
        JAVA_HOME   = 'C:\\Program Files\\Java\\jdk-17'
    }

    stages {
        stage('Surgical Stop') {
            steps {
                echo "Attempting to clear port 8090..."
                catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                    // Terminates existing process to prevent "Port already in use" errors
                    bat 'for /f "tokens=5" %%a in (\'netstat -aon ^| findstr :8090\') do taskkill /f /pid %%a 2>nul || exit 0'
                }
            }
        }

        stage('Maven Build') {
            steps {
                echo "Compiling and packaging fresh code..."
                // Runs the clean package goal to create the latest demo.war
                bat "\"${MAVEN_HOME}\\mvn.cmd\" clean package -DskipTests"
            }
        }

        stage('Surgical Clean and Deploy') {
            steps {
                echo "Removing old folder cache and deploying new WAR..."
                // Step 1: Deletes 'demo' folder so Tomcat is forced to re-extract fresh code
                bat "if exist \"${TOMCAT_PATH}\\webapps\\demo\" rd /s /q \"${TOMCAT_PATH}\\webapps\\demo\""
                // Step 2: Copies the new WAR file to the deployment directory
                bat "xcopy /y \"target\\demo.war\" \"${TOMCAT_PATH}\\webapps\\*\""
            }
        }

        stage('Start Tomcat') {
            steps {
                echo "Launching Tomcat and detaching process..."
                // JENKINS_NODE_COOKIE is the key to keeping Tomcat alive after build ends
                withEnv(['JENKINS_NODE_COOKIE=dontKillMe']) {
                    bat """
                        set "JAVA_HOME=${JAVA_HOME}"
                        set "JRE_HOME=${JAVA_HOME}"
                        cd /d "${TOMCAT_PATH}\\bin"
                        :: Start Tomcat in its own window so it doesn't close with Jenkins
                        start "TomcatServer" cmd /c startup.bat
                    """
                }
                echo "Waiting 25 seconds for Spring Boot and Tomcat to fully initialize..."
                // Native Jenkins sleep is more stable than Windows timeout
                sleep 25
            }
        }
    }

    post {
        success {
            echo "------------------------------------------------------------"
            echo "DEPLOYMENT SUCCESSFUL!"
            echo "ACCESS LATEST DATA AT: http://localhost:8090/demo/users"
            echo "------------------------------------------------------------"
        }
        failure {
            echo "------------------------------------------------------------"
            echo "BUILD FAILED. Please check the Console Output above."
            echo "------------------------------------------------------------"
        }
    }
}