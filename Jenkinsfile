pipeline {
    agent any

    triggers {
        pollSCM('* * * * *') 
    }

    environment {
        TOMCAT_PATH = 'E:\\apache-tomcat-10.1.28'
        MAVEN_HOME  = 'C:\\Users\\heg\\.m2\\wrapper\\dists\\apache-maven-3.9.12\\59fe215c0ad6947fea90184bf7add084544567b927287592651fda3782e0e798\\bin'
        JAVA_HOME   = 'C:\\Program Files\\Java\\jdk-17'
        // Replace this with the token you generated in SonarQube
        SONAR_TOKEN = 'sqa_b77a0c65feff9e8f0bcd782da843b9dfe8d7c640' 
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
                echo "Compiling code..."
                bat "\"${MAVEN_HOME}\\mvn.cmd\" clean compile -DskipTests"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "Scanning code for bugs and vulnerabilities..."
                // This command tells Maven to run the Sonar scanner
                bat "\"${MAVEN_HOME}\\mvn.cmd\" sonar:sonar -Dsonar.login=${SONAR_TOKEN} -Dsonar.host.url=http://localhost:9000"
            }
        }

        stage('Package WAR') {
            steps {
                echo "Creating final demo.war..."
                bat "\"${MAVEN_HOME}\\mvn.cmd\" package -DskipTests"
            }
        }

        stage('Deploy') {
            steps {
                echo "Deploying to Tomcat..."
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
                        start /B startup.bat
                    """
                }
                sleep 20
            }
        }
    }

    post {
        success {
            echo "------------------------------------------------------------"
            echo "SUCCESS: Code scanned and app is live at http://localhost:8090/demo/users"
            echo "------------------------------------------------------------"
        }
    }
}