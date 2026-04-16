pipeline {
    agent any

    triggers {
        pollSCM('* * * * *') 
    }

    environment {
        TOMCAT_PATH = 'E:\\apache-tomcat-10.1.28'
        MAVEN_HOME  = 'C:\\Users\\heg\\.m2\\wrapper\\dists\\apache-maven-3.9.12\\59fe215c0ad6947fea90184bf7add084544567b927287592651fda3782e0e798\\bin'
        JAVA_HOME   = 'C:\\Program Files\\Java\\jdk-17'
    }

    stages {
        stage('Surgical Stop') {
            steps {
                echo "Clearing port 8090..."
                catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                    bat 'for /f "tokens=5" %%a in (\'netstat -aon ^| findstr :8090\') do taskkill /f /pid %%a 2>nul'
                }
            }
        }

        stage('Maven Build') {
            steps {
                echo "Compiling latest code from GitHub..."
                bat "\"${MAVEN_HOME}\\mvn.cmd\" clean package -DskipTests"
            }
        }

        stage('Deploy') {
            steps {
                echo "Wiping old cache and deploying new WAR..."
                bat "if exist \"${TOMCAT_PATH}\\webapps\\demo\" rd /s /q \"${TOMCAT_PATH}\\webapps\\demo\""
                bat "xcopy /y \"target\\demo.war\" \"${TOMCAT_PATH}\\webapps\\*\""
            }
        }

        stage('Start Server') {
            steps {
                echo "Launching Tomcat using Freestyle method..."
                withEnv(['BUILD_ID=dontKillMe']) {
                    bat """
                        set "JAVA_HOME=${JAVA_HOME}"
                        set "JRE_HOME=${JAVA_HOME}"
                        cd /d "${TOMCAT_PATH}\\bin"
                        start "" /B cmd /C startup.bat
                    """
                }
                echo "Waiting 15 seconds for Spring Boot initialization..."
                sleep 15
            }
        }
    }

    post {
        success {
            echo "------------------------------------------------------------"
            echo "SUCCESS: App is live at http://localhost:8090/demo/users"
            echo "------------------------------------------------------------"
        }
    }
}