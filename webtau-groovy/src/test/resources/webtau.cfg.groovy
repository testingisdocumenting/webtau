url = "http://localhost:8180"
configValue = 'testUser'
list = [1, 2, 3, 4]

environments {
   dev {
       url = "http://dev.host:8080"
       list = []
   }

   prod {
       reportGenerator = { report ->
           def path = cfg.reportPath.toAbsolutePath().parent.resolve('custom-report.txt')
           println "custom report path: $path"
           path.text = 'test report'
       }
   }
}