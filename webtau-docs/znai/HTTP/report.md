# Location

By default report is generated at `<workingdir>/webtau.report.html`. To change the location use `--reportPath` option. 

# Summary

Out of the box report provides high level information like number of failed tests and HTTP Operations coverage.

:include-image: doc-artifacts/reports/report-summary.png {fit: true}

# Test Summary

Selected test to see a summary information about test run
:include-image: doc-artifacts/reports/report-test-summary.png {fit: true}

# Navigation

Report is a self contained single page application. 
Url tracks your navigation through screens, so you can share url with your teammates to narrow down a problem.  

# Additional Reports

To generate custom reports or upload report data to your server, specify a `reportGenerator` config property.

:include-file: scenarios/rest/report/webtau.cfg.groovy {title: "webtau.cfg.groovy", excludeRegexp: "package"}

Where `Report.&generateReport` is implemented as following

:include-file: scenarios/rest/report/Report.groovy {title: "scenarios/rest/report/Report.groovy"}

