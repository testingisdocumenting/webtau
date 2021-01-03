# Living Document

In your test you can capture input and output and save it to a file. 
By using documentation systems you can use the captured artifacts to render business friendly documentation of your business logic.

As system evolves, so do your tests and so does your documentation. Essentially you will have a living document describing your system.   

# Capture Input

Use `doc.capture` to save a test captured value to a file.
Example below assumes `core` static import.

:include-file: org/testingisdocumenting/webtau/data/PeopleDaoWithDocTest.java {includeRegexp: "import.*Core"}

:include-java: org/testingisdocumenting/webtau/data/PeopleDaoWithDocTest.java {title: "capturing a value", entry: "initEmployees", commentsType: "inline", bodyOnly: true}

# Capture Expected Output 

Use `doc.expected.capture` to save most recent expected value.

:include-java: org/testingisdocumenting/webtau/data/PeopleDaoWithDocTest.java {title: "capturing most recent expected", entry: "validateNewJoiners", commentsType: "inline", bodyOnly: true}

# Znai Example

Since this documentation is rendered using [Znai](https://github.org/testingisdocumenting/znai) here is an example of how to use the captured artifacts.
[Znai](https://github.org/testingisdocumenting/znai) has `:include-table:` plugin to render a table giving a json or CSV file.

```text
:include-table: doc-artifacts/all-employees.json
:include-table: doc-artifacts/new-joiners.json
```

Below is the example of the business logic rendered as documentation.

---

Our HR system consider all employees that has been in the company less than a month as new joiners.
For example, giving employees:
 
:include-table: doc-artifacts/all-employees.json

System will list following employees as new joiners:

:include-table: doc-artifacts/new-joiners.json