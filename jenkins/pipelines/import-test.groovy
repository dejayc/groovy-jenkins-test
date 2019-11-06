pipeline {
  agent any

  stages {
    stage('Invoke sibling...') {
      steps {
        echo 'Invoking sibling...'
      }
    }
  }
}
