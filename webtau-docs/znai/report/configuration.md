# Report Path

Use `:identifier: reportPath {validationPath: "org/testingisdocumenting/webtau/cfg/WebTauConfig.java"}` to change 
path of a produced self-contained rich HTML report

```tabs
CLI: :include-cli-command: webtau scenarios/*.groovy --reportPath my-report-name.html
Maven: 
:include-file: maven/plugin-report-parameters.xml {
    excludeRegexp: ["failedReportPath", "reportName", "reportNameUrl"],
    highlight: "reportPath"
}
```

# Failed Report Path

Use `:identifier: failedReportPath {validationPath: "org/testingisdocumenting/webtau/cfg/WebTauConfig.java"}` to 
use different path for failed cases.

```tabs
CLI: :include-cli-command: webtau scenarios/*.groovy --failedReportPath my-failed-report-name.html
Maven: 
:include-file: maven/plugin-report-parameters.xml {
    excludeRegexp: ["reportPath", "reportName", "reportNameUrl"],
    highlight: "failedReportPath"
}
```

One of the use-cases is to configure CI upload only failed test reports, especially if you run multiple independent test suits 

```yaml {title: "GitHub actions upload only failed reports"}
- name: Upload failed test reports
  uses: actions/upload-artifact@v2
  if: failure()
  with:
    name: failed suits 
    path: "testing/src/test/groovy/*failed*.html"
    retention-days: 2
```

# Report Name And URL

Use `:identifier: reportName {validationPath: "org/testingisdocumenting/webtau/cfg/WebTauConfig.java"}` and
`:identifier: reportNameUrl {validationPath: "org/testingisdocumenting/webtau/cfg/WebTauConfig.java"}` to change 
default report name.

By default, when you click a report name, browser opens [WebTau](https://github.com/testingisdocumenting/webtau).

Specify `:identifier: reportNameUrl {validationPath: "org/testingisdocumenting/webtau/cfg/WebTauConfig.java"}` to override a page
that opens when you click a report name.


```tabs
CLI: :include-cli-command: webtau scenarios/*.groovy --reportName "my service" --reportNameUrl "https://service-under-test"
Maven: 
:include-file: maven/plugin-report-parameters.xml {
    excludeRegexp: ["reportPath", "failedReportPath"],
    highlight: ["reportName", "reportNameUrl"]
}
```

:include-image: doc-artifacts/reports/report-name.png {fit: true, annotate: true}
