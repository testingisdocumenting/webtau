---
identifier: {validationPath: ["com/example/tests/junit5/WebSocketSpringBootTest.java", "org/testingisdocumenting/webtau/websocket/WebSocketConfig.java"]}
---

# Asynchronous To Synchronous

:include-java-doc: org/testingisdocumenting/webtau/websocket/WebSocketSession.java { entry: "received" }

# Wait For A Specific Message

Use `:identifier: wsSession.received` and `waitTo` to make sure a specific message was received. WebTau will comb through and discard all the received messages that do not match a criteria.
Messages are processed in receive order.

```tabs
Groovy:
:include-file: scenarios/websocket/springBoot.groovy {
  title: "connect, send and wait example", 
  surroundedBy: "connect-send-wait", 
  exclude: [".send", ".close"]
}
:include-markdown: import-ref.md

Java:
:include-file: com/example/tests/junit5/WebSocketSpringBootTest.java {
  title: "connect, send and wait example", 
  surroundedBy: "connect-send-wait",
  exclude: [".send", ".close"]
}
:include-markdown: import-ref.md
```

:include-cli-output: doc-artifacts/springBoot.groovy-console-output.txt {
  title: "console output",
  surroundedBy: "wait until receive message",
  startLine: "waiting", 
  endLine: "received"
}

# Wait For A Specific Message Using Value Path

Use `get(path)` to narrow to a specific response value

```tabs
Groovy:
:include-file: scenarios/websocket/springBoot.groovy {
  title: "wait on specific response value", 
  surroundedBy: "received-get"
}

 Note: Groovy dynamic nature allows you bypass explicit `get`

Java:
:include-file: com/example/tests/junit5/WebSocketSpringBootTest.java {
  title: "wait on specific response value", 
  surroundedBy: "received-get"
}
```

:include-cli-output: doc-artifacts/springBoot.groovy-console-output.txt {
  title: "console output",
  surroundedBy: "wait until receive message using path",
  startLine: "waiting",
  endLine: "received"
}

Use `[idx]` To deal with a list response:

```tabs
Groovy:
:include-file: scenarios/websocket/springBoot.groovy {
  title: "wait on specific response value within list", 
  surroundedBy: "received-list"
}

Java:
:include-file: com/example/tests/junit5/WebSocketSpringBootTest.java {
  title: "wait on specific response value within list", 
  surroundedBy: "received-list"
}
```

:include-cli-output: doc-artifacts/springBoot.groovy-console-output.txt {
  title: "console output",
  surroundedBy: "wait until receive message using path list",
  startLine: "waiting",
  endLine: "received"
}

# Poll Message As Text

:include-java-doc: org/testingisdocumenting/webtau/websocket/WebSocketMessages.java { entry: "pollAsText" }

In example above, we wait until the price becomes greater than 100. All the messages including the matched one are discarded after `waitTo`.

`:identifier: received.pollAsText` returns next received message or wait for the message to come:

```tabs
Groovy:
:include-file: scenarios/websocket/springBoot.groovy {
  title: "poll message", 
  surroundedBy: "poll-after-wait",
  commentsType: "inline"
}

Java:
:include-file: com/example/tests/junit5/WebSocketSpringBootTest.java {
  title: "poll message", 
  surroundedBy: "poll-after-wait",
  commentsType: "inline"
}
```

Note: if there are no already received messages, and no new message arrives within a wait time, `null` will be returned.

# Poll Message As Object

Use `:identifier: received.poll` to convert JSON message to a list or a map. 

```tabs
Groovy:
:include-file: scenarios/websocket/springBoot.groovy {
  title: "poll message as map", 
  surroundedBy: "poll-as-map"
}

Java:
:include-file: com/example/tests/junit5/WebSocketSpringBootTest.java {
  title: "poll message as map", 
  surroundedBy: "poll-as-map"
}
```

# Max Number Of Messages

By default, WebTau keeps only 1000 messages. If new messages arrive, the old ones get discarded. 
Use `:identifier: webSocketMaxMessages` config value to change number of messages to keep.

# Number Of Received Messages

Use `:identifier: received.count` to wait for a certain number of messages:

```tabs
Groovy:
:include-file: scenarios/websocket/springBoot.groovy {
  title: "poll message", 
  include: "received.count"
}

Java:
:include-file: com/example/tests/junit5/WebSocketSpringBootTest.java {
  title: "poll message", 
  include: "received.count"
}
```

:include-cli-output: doc-artifacts/com.example.tests.junit5.WebSocketSpringBootTest-console-output.txt {
  title: "console output",
  startLine: "count",
  endLine: "count"
}

# Discard Messages

Use `:identifier: received.discard` to remove all already received messages. 
Next `:identifier: received.pollAsText` will wait for a new message to arrive as all the received messages will be discarded.

```tabs
Groovy:
:include-file: scenarios/websocket/springBoot.groovy {
  title: "discard messages", 
  surroundedBy: "discard-poll"
}

Java:
:include-file: com/example/tests/junit5/WebSocketSpringBootTest.java {
  title: "discard messages", 
  surroundedBy: "discard-poll"
}
```

:include-cli-output: doc-artifacts/com.example.tests.junit5.WebSocketSpringBootTest-console-output.txt {
  title: "console output",
  startLine: "discarded",
  endLine: "no new message is polled"
}
