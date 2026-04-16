pipeline {
    agent any

    triggers {
        pollSCM('* * * * *') 
    }

    environment {
        // --- Core Paths ---
        MAVEN_HOME  = 'C:\\Users\\heg\\.m2\\wrapper\\dists\\apache-maven-3.9.12\\59fe215c0ad6947fea90184bf7add084544567b927287592651fda3782e0e798\\bin'
        JAVA_HOME   = 'C:\\Program Files\\Java\\jdk-17'
        SONAR_TOKEN = 'sqa_b77a0c65feff9e8f0bcd782da843b9dfe8d7c640'
        IMAGE_NAME  = 'user-service-app'
    }

    stages {
        stage('Surgical Stop') {
            steps {
                echo "Stopping old Docker container and clearing port 8080..."
                catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                    // Stop and remove the old container so we can start a fresh one
                    bat "docker stop ${IMAGE_NAME} || exit 0"
                    bat "docker rm ${IMAGE_NAME} || exit 0"
                }
            }
        }

        stage('Maven Build & Sonar') {
            steps {
                echo "Compiling code and running SonarQube scan..."
                bat "\"${MAVEN_HOME}\\mvn.cmd\" -s \"C:\\Users\\heg\\.m2\\settings.xml\" clean package sonar:sonar -Dsonar.login=${SONAR_TOKEN} -Dsonar.host.url=http://localhost:9000 -DskipTests"
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker Image: ${IMAGE_NAME}..."
                // This uses the Dockerfile in your root folder
                bat "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Docker Run') {
            steps {
                echo "Launching container... Mapping Local 8090 to Docker 8080"
                // -d runs it in the background, -p maps your PC port to the Docker port
                bat "docker run -d --name ${IMAGE_NAME} -p 8090:8080 ${IMAGE_NAME}"
            }
        }
    }

    post {
        success {
            echo "------------------------------------------------------------"
            echo "SUCCESS: Docker container is running!"
            echo "URL: http://localhost:8080/users"
            echo "------------------------------------------------------------"
        }
    }
}