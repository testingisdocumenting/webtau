---
identifier: {validationPath: ["com/example/tests/junit5/WebSocketSpringBootTest.java", "org/testingisdocumenting/webtau/websocket/WebSocketConfig.java"]}
---

# Connection Header

Use `:identifier: websocket.header` as a parameter to `:identifier: websocket.connect` to pass connection header:

```tabs
Groovy:
:include-file: scenarios/websocket/springBoot.groovy {
  title: "poll message as map", 
  surroundedBy: "connect-header"
}
:include-markdown: import-ref.md

Java:
:include-file: com/example/tests/junit5/WebSocketSpringBootTest.java {
  title: "poll message as map", 
  surroundedBy: "connect-header"
}
:include-markdown: import-ref.md
```
