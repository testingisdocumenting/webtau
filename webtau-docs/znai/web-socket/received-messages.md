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

:include-cli-output: doc-artifacts/com.example.tests.junit5.WebSocketSpringBootTest-console-output.txt {
  title: "console output",
  startLine: "waiting", 
  endLine: "received"
}

# Poll Message

:include-java-doc: org/testingisdocumenting/webtau/websocket/WebSocketValue.java { entry: "pollAsText" }

In example above, we wait until the price becomes greater than 100. All the messages including the matched one are discarded after `waitTo`.

`:identifier: received.pollAsText` returns next received message or wait for the message to come:

```tabs
Groovy:
:include-file: scenarios/websocket/springBoot.groovy {
  title: "poll message", 
  surroundedBy: "poll-after-wait"
}

Java:
:include-file: com/example/tests/junit5/WebSocketSpringBootTest.java {
  title: "poll message", 
  surroundedBy: "poll-after-wait"
}
```

# Max Number Of Messages

By default, WebTau keeps only 1000 messages. If new messages arrive, the old ones get discarded. 
Use `:identifier: webSocketMaxMessages` config value to change number of messages to keep.
