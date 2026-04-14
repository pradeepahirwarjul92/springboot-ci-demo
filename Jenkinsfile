pipeline {
    agent any

    options {
        timeout(time: 45, unit: 'MINUTES')
    }

    environment {
        // --- 1. System Paths ---
        JAVA_HOME      = 'C:\\Program Files\\Java\\jdk-17' 
        PROJECT_DIR    = 'E:\\CICD'
        
        // --- 2. Maven Configuration ---
        MVN_EXECUTABLE = 'C:\\Users\\heg\\.m2\\wrapper\\dists\\apache-maven-3.9.12\\59fe215c0ad6947fea90184bf7add084544567b927287592651fda3782e0e798\\bin\\mvn.cmd'
        MVN_SETTINGS   = 'C:\\Users\\heg\\.m2\\settings.xml'

        // --- 3. Proxy & Docker Config ---
        PROXY_HOST     = '192.168.9.112'
        PROXY_PORT     = '808'
        IMAGE_NAME     = 'demo-app'
        TRIVY_IMAGE    = 'ghcr.io/aquasecurity/trivy:latest'
        
        // --- 4. Nexus Configuration ---
        NEXUS_URL      = 'localhost:8083'
    }

    stages {
        stage('Build with Maven') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'proxy-creds', usernameVariable: 'PUSER', passwordVariable: 'PPASS')]) {
                    dir("${env.PROJECT_DIR}") {
                        echo 'Building JAR...'
                        bat """
                        @echo on
                        set PROXY_URL=http://%PUSER%:%PPASS%@%PROXY_HOST%:%PROXY_PORT%
                        set http_proxy=%PROXY_URL%
                        set https_proxy=%PROXY_URL%
                        "${env.MVN_EXECUTABLE}" -s "${env.MVN_SETTINGS}" clean package -DskipTests -U
                        """
                    }
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool 'SonarScanner'
                    dir("${env.PROJECT_DIR}") {
                        withSonarQubeEnv('SonarQube') {
                            withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                                bat """
                                "${scannerHome}\\bin\\sonar-scanner.bat" ^
                                -Dsonar.projectKey=DemoProject ^
                                -Dsonar.sources=src/main/java ^
                                -Dsonar.java.binaries=target/classes ^
                                -Dsonar.token=%SONAR_TOKEN% ^
                                -Dsonar.host.url=http://localhost:9000
                                """
                            }
                        }
                    }
                }
            }
        }

        stage('Docker Build & Tag') {
            steps {
                dir("${env.PROJECT_DIR}") {
                    echo 'Building and Tagging for Nexus Repository...'
                    bat "docker build -t ${env.IMAGE_NAME}:latest ."
                    bat "docker tag ${env.IMAGE_NAME}:latest ${env.NEXUS_URL}/${env.IMAGE_NAME}:latest"
                }
            }
        }

        stage('Security Scan (Trivy)') {
            steps {
                echo 'Running Lightweight OS Scan...'
                // We use --vuln-type os to skip the 800MB Java DB download that causes proxy errors
                bat """
                docker run --rm -v //var/run/docker.sock:/var/run/docker.sock ^
                -v "%cd%:/apps" ^
                ${env.TRIVY_IMAGE} image --format template --template "@contrib/html.tpl" ^
                --severity HIGH,CRITICAL --timeout 15m --vuln-type os ^
                -o /apps/trivy-report.html ${env.IMAGE_NAME}:latest
                """
                archiveArtifacts artifacts: 'trivy-report.html', allowEmptyArchive: false
            }
        }

        stage('Push to Nexus') {
            steps {
                echo 'Uploading Image to Nexus Repository...'
                // This command will now push the image to your docker-private repo
                bat "docker push ${env.NEXUS_URL}/${env.IMAGE_NAME}:latest"
            }
        }

        stage('Docker Run & Verify') {
            steps {
                dir("${env.PROJECT_DIR}") {
                    echo 'Deploying Container...'
                    bat "docker stop ${env.IMAGE_NAME} || exit 0"
                    bat "docker rm ${env.IMAGE_NAME} || exit 0"
                    bat "docker run -d --name ${env.IMAGE_NAME} -p 8081:8090 -e SERVER_SERVLET_CONTEXT_PATH=/demo ${env.IMAGE_NAME}:latest"
                    
                    sleep 15
                    bat "docker logs ${env.IMAGE_NAME}"
                }
            }
        }
    }

    post {
        success {
            echo "🚀 PIPELINE COMPLETE!"
            echo "App: http://localhost:8081/demo/users"
            echo "Nexus: http://localhost:8083"
        }
        failure {
            echo "❌ Pipeline failed. Check the logs above."
        }
    }
}