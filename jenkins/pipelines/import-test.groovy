def imports = [:]

pipeline {
  agent any

  stages {
    stage('Invoke sibling...') {
      steps {
        echo 'Invoking sibling...'
        script {
          imports.test = load 'jenkins/lib/test.groovy'
          imports.test.test('hello!')
        }
      }
    }
  }
}
