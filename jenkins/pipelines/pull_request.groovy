// Check ./README.md for more details.
// Set up Jenkins to only trigger this on code change
// e.g. $PR_ACTION in ['opened', 'reopened', 'synchronize']
// Code checks requiring a build happen here.
// * Linter
// * Test

pipeline {
  agent any

  environment {
    NODEPATH = '/var/lib/jenkins/tools/jenkins.plugins.nodejs.tools.NodeJSInstallation/Node8/bin'
    PATH = "$NODEPATH:$PATH"
    PIPELINE = 'Jenkins PR Test/Lint Pipeline'
  }

  stages {
    stage('Notify GitHub') {
      steps {
        setBuildStatus("Build pending", "PENDING", "$PIPELINE");
      }
    }
    stage('Build') {
      steps {
        echo 'Building...'
        sh 'npm install'
      }
    }
    stage('Test') {
      steps {
        echo 'Running Tests...'
        sh 'npm test'
      }
    }
    stage('Linter') {
      steps {
        echo 'Running Linter...'
        sh 'npm run lint'
      }
    }
  }

  post {
    success {
      setBuildStatus("Build succeeded", "SUCCESS", "$PIPELINE");
    }
    failure {
      setBuildStatus("Build failed", "FAILURE", "$PIPELINE");
    }
  }
}

void setBuildStatus(String message, String state, String context) {
  step([
    $class: "GitHubCommitStatusSetter",
    reposSource: [
      $class: "ManuallyEnteredRepositorySource",
      url: "https://github.com/cnbc/WEB.Phoenix"
    ],
    contextSource: [
      $class: "ManuallyEnteredCommitContextSource",
      context: context
    ],
    errorHandlers: [
      [
        $class: "ChangingBuildStatusErrorHandler",
        result: "UNSTABLE"
      ]
    ],
    statusResultSource: [
      $class: "ConditionalStatusResultSource",
      results: [
        [
          $class: "AnyBuildResult",
          message: message,
          state: state
        ]
      ]
    ]
  ]);
}
