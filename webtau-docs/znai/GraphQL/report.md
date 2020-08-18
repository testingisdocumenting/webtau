# Additional Reports

To generate custom reports, or upload report data to your server, specify `reportGenerator` config property.

:include-file: examples/scenarios/graphql/webtau-report.cfg.groovy {title: "webtau.cfg.groovy"}

Where `Report.&generateReport` is implemented as follows:

:include-file: examples/scenarios/graphql/Report.groovy {title: "scenarios/graphql/Report.groovy", commentsType: "inline"}

The output looks as follows:

:include-file: examples/webtau.graphql-report.json {title: "webtau.graphql-report.json"} 

## Coverage and timing statistics

WebTau will implicitly invoke your GraphQL server's introspection queries in order to fetch a subset of the schema.
It uses this schema in conjunction with the requests in tests to compute:

* query coverage - which queries were invoked by tests and which were not as well as overall summary of coverage
* timing information - http call timing statistics by query
