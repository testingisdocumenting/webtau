url = "http://localhost:8180"
configValue = 'testUser'

additionalUrls {
    appOne = "http://app-one"
    appTwo = "http://app-two"
}

parent.child = 'pc-1'
parent.another = 'pc-2'

environments {
    dev {
        additionalUrls {
            appOne = "http://app-one-dev"
        }
    }

    prod {
        additionalUrls.appOne = "http://app-one-prod"
        additionalUrls {
            appTwo = "http://app-two-prod"
        }

        reportGenerator = { report ->
            def path = cfg.reportPath.toAbsolutePath().parent.resolve('custom-report.txt')
            println "custom report path: $path"
            path.text = 'test report ' + entries.testEntries.size()
        }
    }
}