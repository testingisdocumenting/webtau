# Fake Server Creation

Use `:identifier: server.fake {validationPath: "scenarios/server/fakeRest.groovy"}` to create a server with controlled responses

:include-file: scenarios/server/fakeRest.groovy {
  title: "fake server creation example",
  includeRegexp: "router-example",
  commentsType: "remove"
}

:include-file: scenarios/server/fakeRest.groovy {
  title: "fake server creation example",
  surroundedBy: "fake-response-check"
}


# Server Slowdown

Use `:identifier: markUnresponsive {validationPath: "scenarios/server/fakeRest.groovy"}` to mark server as unresponsive

:include-file: scenarios/server/fakeRest.groovy {
  title: "mark as unresponsive",
  surroundedBy: "mark-unresponsive",
}

# Server Break

Use `:identifier: markBroken {validationPath: "scenarios/server/fakeRest.groovy"}` to mark server as broken

:include-file: scenarios/server/fakeRest.groovy {
  title: "mark as broken",
  surroundedBy: "mark-broken",
}

# Server Fix

Use `:identifier: fix {validationPath: "scenarios/server/fakeRest.groovy"}` to remove broken and/or slowdown state

:include-file: scenarios/server/fakeRest.groovy {
  title: "fix server",
  surroundedBy: "mark-fix"
}


