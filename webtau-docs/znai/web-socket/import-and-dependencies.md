:include-markdown: imports/single-dsl-import.md

# WebSocket Specific Import

To only import `websocket` module use

```tabs
Groovy:
:include-file: com/example/tests/junit5/WebSocketSpringBootTest.java {title: "websocket import", includeRegexp: "websocket.WebSocket.websocket", replace: [";", ""]} 
:include-file: maven/websocket-dep.xml {title: "maven dependency"}

Java:
:include-file: com/example/tests/junit5/WebSocketSpringBootTest.java {title: "websocket import", includeRegexp: "websocket.WebSocket.websocket"} 
:include-file: maven/websocket-dep.xml {title: "maven dependency"}
```
