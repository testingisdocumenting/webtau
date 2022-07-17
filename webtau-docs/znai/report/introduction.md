# Summary

Out of the box report provides high level information like number of failed tests and HTTP Operations coverage.

:include-image: doc-artifacts/reports/report-summary.png {fit: true}

# Test Summary

Selected a test to see summary information about the test run

:include-image: doc-artifacts/reports/report-test-summary.png {fit: true}

# Permalinks

Report is a self-contained single page application. You can email it, upload to slack, put on a shared drive without an HTTP server. 
Url tracks your navigation through screens, so you can share url to a specific problem. 

# Data Tracking 

WebTau tracks all actions and assertions tests perform. 
Actions and assertions are printed to console as well as available in the generated HTML report.

In a test details screen, there are multiple tabs.  
1. Layer specific details, e.g. HTTP, CLI, DB, Servers, etc
2. Every action that was performed

:include-image: doc-artifacts/reports/report-crud-http-calls.png {border: true, annotate: true, annotationsPath: "doc-artifacts/reports/report-crud-steps.json", fit: true}

:include-image: doc-artifacts/reports/report-crud-steps.png {border: true, fit: true}

Every WebTau standard action generates `WebTauStep`. You can use WebTau API to wrap your repeating actions 
into WebTau compatible steps. 
