# Serve Static Content

Use `:identifier: server.serve {validationPath: "scenarios/server/staticContent.groovy"}` to start a server on random port
that will host static content from the specified directory.

:include-file: scenarios/server/staticContent.groovy {
  title: "create and start server",
  surroundedBy: "static-server-create",
}

:include-file: data/staticcontent/data.json {autoTitle: true}

:include-file: scenarios/server/staticContent.groovy {
  title: "testing response from a static server",
  surroundedBy: "static-server-json",
}

# Host Html For Browser

Use static server to host html files and then open them using a browser.

:include-file: data/staticcontent/hello.html {autoTitle: true}

:include-file: scenarios/server/staticContent.groovy {
  title: "open browser using static contet",
  surroundedBy: "static-server-html",
}

# Override Response

Use `:identifier: addOverride {validationPath: "scenarios/server/proxyServer.groovy"}` to modify response of a proxied server

:include-file: scenarios/server/staticContent.groovy {
  title: "content override",
  surroundedBy: "override-example",
}

# Server Slowdown

Use `:identifier: markUnresponsive {validationPath: "scenarios/server/staticContent.groovy"}` to mark server as unresponsive

:include-file: scenarios/server/staticContent.groovy {
  title: "mark as unresponsive",
  surroundedBy: "mark-unresponsive",
}

# Server Break

Use `:identifier: markBroken {validationPath: "scenarios/server/staticContent.groovy"}` to mark server as broken

:include-file: scenarios/server/staticContent.groovy {
  title: "mark as broken",
  surroundedBy: "mark-broken",
}

# Server Fix

Use `:identifier: fix {validationPath: "scenarios/server/staticContent.groovy"}` to remove broken and/or slowdown state

:include-file: scenarios/server/staticContent.groovy {
  title: "fix server",
  surroundedBy: "mark-fix"
}

