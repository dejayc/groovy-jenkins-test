// Check ./README.md for more details.
// Quick code checks happen here.
// * Danger

pipeline {
  agent any

  environment {
    NODEPATH = '/var/lib/jenkins/tools/jenkins.plugins.nodejs.tools.NodeJSInstallation/Node8/bin'
    PATH = "$NODEPATH:$PATH"
    PIPELINE = 'Jenkins PR Danger Pipeline'
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
    stage('Danger') {
      environment {
        // Danger needs Babel 7 to compile Dangerfile
        // and fails at runtime, because project has Babel 6.
        // Disabling transpilation is a workaround:
        DANGER_DISABLE_TRANSPILATION = 'true'

        // These values are needed to perform
        // updates to the PR
        DANGER_TEST_REPO = "$REPO_NAME"
        DANGER_FAKE_CI = 'YEP'
        DANGER_TEST_PR = "$PR_NUMBER"
      }
      steps {
        echo 'Running Danger...'
        withCredentials([
          string(
            credentialsId: 'github-api-token',
            variable: 'DANGER_GITHUB_API_TOKEN'
          )
        ]) {
          sh 'npm run danger ci'
        }
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
