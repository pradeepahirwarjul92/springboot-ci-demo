pipeline {
    agent any

    triggers {
        pollSCM('* * * * *') 
    }

    environment {
        MAVEN_HOME   = 'C:\\Users\\heg\\.m2\\wrapper\\dists\\apache-maven-3.9.12\\59fe215c0ad6947fea90184bf7add084544567b927287592651fda3782e0e798\\bin'
        JAVA_HOME    = 'C:\\Program Files\\Java\\jdk-17'
        SONAR_TOKEN  = 'sqa_b77a0c65feff9e8f0bcd782da843b9dfe8d7c640'
        IMAGE_NAME   = 'user-service-app'
        SETTINGS_XML = 'C:\\Users\\heg\\.m2\\settings.xml'
        ANSIBLE_DIR  = '~/ansible-project'
    }

    stages {
        stage('Maven Build & Sonar') {
            steps {
                echo "Compiling code and performing SonarQube quality scan..."
                bat "\"${MAVEN_HOME}\\mvn.cmd\" -s \"${SETTINGS_XML}\" clean package sonar:sonar -U -Dsonar.login=${SONAR_TOKEN} -Dsonar.host.url=http://localhost:9000 -DskipTests"
            }
        }

        // NEW STAGE: Frees up ~1GB of RAM for the rest of the pipeline
        stage('Resource Cleanup') {
            steps {
                echo "Freeing up RAM: Terminating Build Tools..."
                bat "docker stop sonarqube nexus || echo Sonarqube already stopped"
            }
        }

        stage('Upload to Nexus') {
            steps {
                echo "Pushing built artifact to Nexus Repository..."
                bat "\"${MAVEN_HOME}\\mvn.cmd\" -s \"${SETTINGS_XML}\" deploy -U -DskipTests"
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker Image: ${IMAGE_NAME}..."
                bat "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Ansible Deployment') {
            steps {
                echo "Handing off to Ansible for professional Deployment..."
                // Added a small sleep to let WSL stabilize after RAM cleanup
                bat "timeout /t 5"
                bat "wsl -d Ubuntu bash -lc \"ansible-playbook -i ~/ansible-project/inventory.ini ~/ansible-project/deploy_java_app.yml\""
            }
        }
    }

    post {
        success {
            echo "---------------------------------------------------------------------------------"
            echo "CI/CD PIPELINE COMPLETE!"
            echo "1. Code Quality: http://localhost:9000 (Note: Container stopped to save RAM)"
            echo "2. Nexus Artifact: http://localhost:8081/#browse/browse:maven-releases"
            echo "3. Docker App (Ansible Deployed): http://localhost:9090/demo/users"
            echo "---------------------------------------------------------------------------------"
        }
        failure {
            echo "Build Failed. Please check Jenkins Console Output, Nexus/Sonar connectivity, and Windows Services Logon."
        }
    }
}