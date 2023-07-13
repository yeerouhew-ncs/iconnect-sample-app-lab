pipeline {
    agent any

    stages {
       stage ('clean') {

            steps {
                bat 'mvn clean'
            }
        }
		
		stage ('package') {

            steps {
                bat 'mvn package -Pprod,war'
            }
        }
        
		stage('Sonarqube Scan') {
		    environment {
                scannerHome = tool 'default Scanner'
            }
            steps {
                withSonarQubeEnv('CodeSparks SonarQube') {
                    bat '%scannerHome%/bin/sonar-scanner'
                }
            }
        }

		stage ('NexusIQ Scan') {
            steps {
                nexusPolicyEvaluation iqApplication: 'IconnectSampleAppLabApp', iqScanPatterns: [[scanPattern: '**/*.war']], iqStage: 'build'
            }
        }	
    }
}
