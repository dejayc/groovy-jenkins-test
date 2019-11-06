import jenkins.lib.test as libTest

pipeline {
  agent any

  stages {
    stage('Invoke sibling...') {
      steps {
        echo 'Invoking sibling...'
        libTest('hello')
      }
    }
  }
}
