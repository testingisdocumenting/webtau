# Registration

To generate custom reports or upload report data to your server, specify a `reportGenerator` config property.

:include-file: scenarios/rest/report/webtau.cfg.groovy {title: "webtau.cfg.groovy", excludeRegexp: "package"}

Where `Report.&generateReport` is implemented as following

:include-file: scenarios/rest/report/Report.groovy {title: "scenarios/rest/report/Report.groovy"}

# GraphQL Example

Head over to [GraphQL](GraphQL/report) to take a look at custom report to capture additional metrics

