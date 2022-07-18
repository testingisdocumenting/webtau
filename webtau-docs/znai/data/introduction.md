WebTau `data` module streamline access to test data.

Data access generates [Report Steps](report/steps) to help with tests investigation. 

* [json](data/json) - access JSON test data
* [csv](data/csv) - access CSV test data
* [pdf](data/pdf) - access PDF data to validate

:include-file: scenarios/data/readingData.groovy {
  title: "read table data from csv", 
  surroundedBy: "// read" 
}

:include-cli-output: doc-artifacts/csv-table-data-output.txt {title: "console output"}
