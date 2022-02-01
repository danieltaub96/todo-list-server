pipeline {
 environment {
        DOCKER_REGISTRY="https://registry.digitalocean.com/mndy"
        DOCKER_CREDS="mndy_digitalocean_registry"
        IMAGE_NAME = "${DOCKER_REGISTRY.replace('https://', '')}/todo-list-server:${BUILD_NUMBER}"
        NAMESPACE = "todo-list"
    }

    agent any
    stages {
        stage('Compile') {
            steps {
                container('gradle') {
                    sh 'gradle --no-daemon compileJava'
                }
            }
        }
        stage('Test') {
            steps {
                container('gradle') {
                    sh 'gradle --no-daemon test'
                }
            }
        }

        stage('Boot Jar') {
                steps {
                    container('gradle') {
                        sh 'gradle --no-daemon bootJar'
                    }
                }
         }

      stage('Build Docker image and Push') {
            when { branch 'master' }
            steps {
                container('docker') {
                    script {
                        docker.withRegistry(DOCKER_REGISTRY, DOCKER_CREDS) {
                            def image = docker.build(IMAGE_NAME, '-f Dockerfile .')

                            image.push()
                        }
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when { branch 'master' }
            steps {
                container('kubectl') {
                    dir("k8s") {
                        sh "sed -i 's/BUILDNUM/${BUILD_NUMBER}/g' DC.yaml"
                        withKubeConfig([credentialsId: 'mndy_digitalocean_kubeconfig']) {
                            sh "kubectl apply -f DC.yaml --namespace ${NAMESPACE}"
                            sh "kubectl apply -f SVC.yaml --namespace ${NAMESPACE}"
                            sh "kubectl apply -f INGRESS.yaml --namespace ${NAMESPACE}"
                        }
                    }
                }
            }
        }

    }
    post {
        always {
            //deleteDir()
            echo "finished"
        }
    }
}