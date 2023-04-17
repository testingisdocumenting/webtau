# Uploading Reports

WebTau generates [HTML Reports](report/introduction). 
To make reports accessible after your build run, use this step:

:include-file: .github/workflows/ci-build.yaml {
  title: "step to upload all reports",
  startLine: "Upload all reports",
  endLine: "retention-days",
  replace: ["\\*webtau-", "webtau."]
}

To only upload failed reports, configure [failed report name](report/configuration#failed-report-path) and update the step:

:include-file: .github/workflows/ci-build.yaml {
  title: "step to upload only failed test reports",
  startLine: "Upload failed test reports", 
  endLine: "retention-days"
}
