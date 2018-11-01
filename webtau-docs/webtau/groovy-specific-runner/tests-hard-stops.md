# Terminate All

Use `terminateAll` if you need to stop a current scenario and all the scenarios after that. Consequent scenarios will be marked 
as skipped in the produced report.

:include-file: scenarios/concept/testsTermination.groovy {title: "tests termination"}

Test two and three in the example above will not be executed. Browser will not be opened and REST API will not be called.
