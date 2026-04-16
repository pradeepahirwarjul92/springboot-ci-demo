pipeline {
    agent any

    // This section makes it work exactly like your Freestyle "Poll SCM"
    triggers {
        pollSCM('* * * * *') // Checks for changes every 1 minute
    }

    environment {
        TOMCAT_PATH = 'E:/apache-tomcat-10.1.28'
        MAVEN_HOME  = 'C:/Users/heg/.m2/wrapper/dists/apache-maven-3.9.12/59fe215c0ad6947fea90184bf7add084544567b927287592651fda3782e0e798/bin'
        JAVA_HOME   = 'C:/Program Files/Java/jdk-17'
    }

    stages {
        stage('Stop Existing Server') {
            steps {
                echo "Finding and killing process on port 8090..."
                catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                    bat 'for /f "tokens=5" %%a in (\'netstat -aon ^| findstr :8090\') do taskkill /f /pid %%a 2>nul'
                }
            }
        }

        stage('Clean and Build') {
            steps {
                echo "Compiling and Packaging..."
                bat "\"${MAVEN_HOME}/mvn.cmd\" clean package -DskipTests"
            }
        }

        stage('Surgical Clean and Deploy') {
            steps {
                echo "Removing old folder and deploying fresh WAR..."
                bat "if exist \"${TOMCAT_PATH}/webapps/demo\" rd /s /q \"${TOMCAT_PATH}/webapps/demo\""
                bat "xcopy /y \"target\\demo.war\" \"${TOMCAT_PATH}/webapps/\""
            }
        }

        stage('Start Tomcat') {
            steps {
                echo "Starting Server..."
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
            echo "DEPLOYMENT SUCCESSFUL! URL: http://localhost:8090/demo/users"
            echo "------------------------------------------------------------"
        }
    }
}