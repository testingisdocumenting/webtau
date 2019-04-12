# Dynamic Scenarios

Every time you call `scenario` method a new scenario is being registered. 
Define scenarios in a loop to have dynamic scenarios based on provided data.   

:include-file: scenarios/concept/simpleDynamicScenario.groovy {title: "dynamic scenarios"}

# CSV 

Use `data.csv` to conveniently build your scenarios from an external `CSV` data set.

:include-file: examples/use-cases.csv {title: "examples/use-cases.csv"}
 
:include-file: scenarios/concept/dataDrivenCsv.groovy {title: "CSV-driven tests"} 

# Table Data

Use `TableData` if you want to derive data and/or have a convenience of collocating data and tests 

:include-file: scenarios/concept/dataDrivenTableData.groovy {title: "TableData-driven tests"}
