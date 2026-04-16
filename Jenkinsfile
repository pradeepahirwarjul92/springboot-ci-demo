pipeline {
    agent any

    triggers {
        pollSCM('* * * * *') 
    }

    environment {
        MAVEN_HOME  = 'C:\\Users\\heg\\.m2\\wrapper\\dists\\apache-maven-3.9.12\\59fe215c0ad6947fea90184bf7add084544567b927287592651fda3782e0e798\\bin'
        JAVA_HOME   = 'C:\\Program Files\\Java\\jdk-17'
        SONAR_TOKEN = 'sqa_b77a0c65feff9e8f0bcd782da843b9dfe8d7c640'
        IMAGE_NAME  = 'user-service-app'
    }

    stages {
        stage('Surgical Stop') {
            steps {
                echo "Forcing removal of old container to prevent naming conflicts..."
                catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                    // We only kill the Docker container, NOT the local port 8090
                    bat "docker stop ${IMAGE_NAME} 2>nul || exit 0"
                    sleep 2
                    bat "docker rm -f ${IMAGE_NAME} 2>nul || exit 0"
                }
            }
        }

        stage('Maven Build & Sonar') {
            steps {
                echo "Building code and scanning..."
                bat "\"${MAVEN_HOME}\\mvn.cmd\" -s \"C:\\Users\\heg\\.m2\\settings.xml\" clean package sonar:sonar -Dsonar.login=${SONAR_TOKEN} -Dsonar.host.url=http://localhost:9000 -DskipTests"
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker Image..."
                bat "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Docker Run') {
            steps {
                echo "Launching container with /demo context path to match Tomcat..."
                // -p 9090 (PC) : 8090 (Inside Docker)
                bat "docker run -d --name ${IMAGE_NAME} -p 9090:8090 -e SERVER_SERVLET_CONTEXT_PATH=/demo ${IMAGE_NAME}"
            }
        }
    }

    post {
        success {
            echo "------------------------------------------------------------"
            echo "DEPLOYMENT SUCCESSFUL!"
            echo "Local Tomcat: http://localhost:8090/demo/users"
            echo "Docker Container: http://localhost:9090/users"
            echo "------------------------------------------------------------"
        }
    }
}