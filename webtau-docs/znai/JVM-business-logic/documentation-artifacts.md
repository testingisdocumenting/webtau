# Living Document

In your test you can capture input and output and save it to a file. 
By using documentation systems you can use the captured artifacts to render business friendly documentation.

As system evolves, so do your tests and so does your documentation. Essentially you will have a living document describing aspects of your system.   

# Capture Input

Use `doc.capture` to save a test captured value to a file.
Example below assumes `core` static import.

:include-file: org/testingisdocumenting/webtau/data/PeopleDaoWithDocTest.java {includeRegexp: "import.*Core"}

:include-java: org/testingisdocumenting/webtau/data/PeopleDaoWithDocTest.java {title: "capturing a value", entry: "initEmployees", commentsType: "inline", bodyOnly: true}

# Capture Expected Output 

Use `doc.expected.capture` to save most recent expected value.

:include-java: org/testingisdocumenting/webtau/data/PeopleDaoWithDocTest.java {title: "capturing most recent expected", entry: "validateNewJoiners", commentsType: "inline", bodyOnly: true}

WebTau documentation is created using [Znai](https://github.org/testingisdocumenting/znai).
It has `:include-table:` plugin to render a table giving a json or CSV file.

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

# Capture Console Output

Use `doc.console.capture` to capture console output of a provided code block

:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "capture console output",
  entry: "captureConsoleOutputExample",
  bodyOnly: true
}

:include-file: doc-artifacts/http-get-console-output.txt {
  title: "raw content of doc-artifacts/http-get-console-output.txt"
}

[Znai](https://github.org/testingisdocumenting/znai) has `cli-output` plugin to render ANSI output

    :include-cli-output: doc-artifacts/http-get-console-output.txt

:include-cli-output: doc-artifacts/http-get-console-output.txt
