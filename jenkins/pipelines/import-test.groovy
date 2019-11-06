pipeline {
  agent any

  stages {
    stage('Invoke sibling...') {
      steps {
        echo 'Invoking sibling...'
        import jenkins.lib.test as libTest
        libTest('hello')
      }
    }
  }
}
