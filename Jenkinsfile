pipeline {
    agent any

    // This makes Jenkins act like your Freestyle 'Poll SCM'
    triggers {
        pollSCM('* * * * *') 
    }

    options {
        // Prevents multiple builds from running if you save/push many times quickly
        disableConcurrentBuilds()
        // Wait 5 seconds after a change is detected before starting
        quietPeriod(5)
    }

    environment {
        TOMCAT_PATH = 'E:\\apache-tomcat-10.1.28'
        MAVEN_HOME  = 'C:\\Users\\heg\\.m2\\wrapper\\dists\\apache-maven-3.9.12\\59fe215c0ad6947fea90184bf7add084544567b927287592651fda3782e0e798\\bin'
        JAVA_HOME   = 'C:\\Program Files\\Java\\jdk-17'
    }

    stages {
        stage('Stop & Clean Port') {
            steps {
                echo "Finding and killing process on port 8090 to prevent old data caching..."
                catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                    bat 'for /f "tokens=5" %%a in (\'netstat -aon ^| findstr :8090\') do taskkill /f /pid %%a 2>nul || exit 0'
                }
            }
        }

        stage('Build Latest Code') {
            steps {
                echo "Compiling the NEW data you just pushed..."
                bat "\"${MAVEN_HOME}\\mvn.cmd\" clean package -DskipTests"
            }
        }

        stage('Surgical Deploy') {
            steps {
                echo "Wiping OLD 'demo' folder to ensure LATEST data is shown..."
                // This is the specific command that prevents 'Old Data' from showing
                bat "if exist \"${TOMCAT_PATH}\\webapps\\demo\" rd /s /q \"${TOMCAT_PATH}\\webapps\\demo\""
                bat "xcopy /y \"target\\demo.war\" \"${TOMCAT_PATH}\\webapps\\*\""
            }
        }

        stage('Start Server') {
            steps {
                echo "Launching Tomcat..."
                withEnv(['JENKINS_NODE_COOKIE=dontKillMe']) {
                    bat """
                        set "JAVA_HOME=${JAVA_HOME}"
                        set "JRE_HOME=${JAVA_HOME}"
                        cd /d "${TOMCAT_PATH}\\bin"
                        start "TomcatServer" cmd /c startup.bat
                    """
                }
                echo "Waiting for data to refresh..."
                sleep 25
            }
        }
    }

    post {
        success {
            echo "------------------------------------------------------------"
            echo "LATEST DATA IS NOW LIVE AT: http://localhost:8090/demo/users"
            echo "------------------------------------------------------------"
        }
    }
}