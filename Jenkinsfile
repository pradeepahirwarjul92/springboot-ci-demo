pipeline {
    agent any

    triggers {
        // Automatically checks your GitHub for changes every minute
        pollSCM('* * * * *') 
    }

    environment {
        // --- Core Environment Paths for your PC ---
        MAVEN_HOME   = 'C:\\Users\\heg\\.m2\\wrapper\\dists\\apache-maven-3.9.12\\59fe215c0ad6947fea90184bf7add084544567b927287592651fda3782e0e798\\bin'
        JAVA_HOME    = 'C:\\Program Files\\Java\\jdk-17'
        SONAR_TOKEN  = 'sqa_b77a0c65feff9e8f0bcd782da843b9dfe8d7c640'
        IMAGE_NAME   = 'user-service-app'
        SETTINGS_XML = 'C:\\Users\\heg\\.m2\\settings.xml'
    }

    stages {
        stage('Maven Build & Sonar') {
            steps {
                echo "Compiling code and performing SonarQube quality scan..."
                // Uses your settings.xml to bypass proxy for Sonar scan
                bat "\"${MAVEN_HOME}\\mvn.cmd\" -s \"${SETTINGS_XML}\" clean package sonar:sonar -Dsonar.login=${SONAR_TOKEN} -Dsonar.host.url=http://localhost:9000 -DskipTests"
            }
        }

        stage('Upload to Nexus') {
            steps {
                echo "Pushing built artifact to Nexus Repository..."
                // The 'deploy' command sends your .war file to your private library
                bat "\"${MAVEN_HOME}\\mvn.cmd\" -s \"${SETTINGS_XML}\" deploy -DskipTests"
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker Image: ${IMAGE_NAME}..."
                // Packages your .war into a containerized environment
                bat "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Deployment (Bypassing WSL)') {
            steps {
                echo "Deploying container directly to bypass Windows Service WSL restrictions..."
                // Runs the commands directly in Jenkins instead of routing through WSL/Ansible
                bat "docker stop ${IMAGE_NAME} 2>nul || exit 0"
                bat "docker rm -f ${IMAGE_NAME} 2>nul || exit 0"
                bat "docker run -d --name ${IMAGE_NAME} -p 9090:8090 -e SERVER_SERVLET_CONTEXT_PATH=/demo ${IMAGE_NAME}"
            }
        }
    }

    post {
        success {
            echo "---------------------------------------------------------------------------------"
            echo "CI/CD PIPELINE COMPLETE!"
            echo "1. Code Quality: http://localhost:9000"
            echo "2. Nexus Artifact: http://localhost:8081/#browse/browse:maven-releases"
            echo "3. Docker App (Direct Deploy): http://localhost:9090/demo/users"
            echo "---------------------------------------------------------------------------------"
        }
        failure {
            echo "Build Failed. Please check Jenkins Console Output and Nexus/Sonar connectivity."
        }
    }
}