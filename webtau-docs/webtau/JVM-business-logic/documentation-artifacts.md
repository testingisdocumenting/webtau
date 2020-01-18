# Living Document

In your test you can capture input and output and save it to a file. 
By using documentation systems you can use the captured artifacts to render business friendly documentation of your business logic.

As system evolves, so do your tests and so does your documentation. Essentially you will have a living document describing your system.   

# Capture Input

Use `doc.capture` to save any value to a file.
Example below assumes `core` static import.

:include-file: com/twosigma/webtau/data/PeopleDaoWithDocTest.java {includeRegexp: "import.*Core"}

:include-java: com/twosigma/webtau/data/PeopleDaoWithDocTest.java {title: "capturing a value", entry: "initEmployees", commentsType: "inline", bodyOnly: true}

:include-java-doc: com/twosigma/webtau/documentation/CoreDocumentation.java {entry: "capture"}

# Capture Expected Output 

Use `doc.expected.capture` to save most recent expected value.

:include-java: com/twosigma/webtau/data/PeopleDaoWithDocTest.java {title: "capturing most recent expected", entry: "validateNewJoiners", commentsType: "inline", bodyOnly: true}
