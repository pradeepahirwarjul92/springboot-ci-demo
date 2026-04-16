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
                echo "Stopping old Docker container... (Local Tomcat on 8090 will stay running)"
                catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                    // We only kill the Docker container, NOT the local port 8090
                    bat "docker stop ${IMAGE_NAME} || exit 0"
                    bat "docker rm ${IMAGE_NAME} || exit 0"
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
                echo "Launching container on PORT 9090 to avoid conflict with Local Tomcat"
                // -p 9090 (PC) : 8080 (Inside Docker)
                bat "docker run -d --name ${IMAGE_NAME} -p 9090:8080 ${IMAGE_NAME}"
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