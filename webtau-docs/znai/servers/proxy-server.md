# Proxy Servers Creation

Use `:identifier: server.proxy {validationPath: "scenarios/server/proxyServer.groovy"}` to create a proxy server by specifying target url

:include-file: scenarios/server/proxyServer.groovy {
  title: "proxy server creation example",
  surroundedBy: "proxy-server-creation",
}

# Override Response

Use `:identifier: addOverride {validationPath: "scenarios/server/proxyServer.groovy"}` to modify response of a proxied server

:include-file: scenarios/server/proxyServer.groovy {
  title: "content override",
  surroundedBy: "proxy-add-override",
}

# Server Slowdown 

Use `:identifier: markUnresponsive {validationPath: "scenarios/server/proxyServer.groovy"}` to mark server as unresponsive 

:include-file: scenarios/server/proxyServer.groovy {
  title: "mark as unresponsive",
  surroundedBy: "mark-unresponsive",
}

:include-file: scenarios/server/proxyServer.groovy {
  title: "unresponsive timeout",
  surroundedBy: "unresponsive-time-out-throw",
}

# Server Break 

Use `:identifier: markBroken {validationPath: "scenarios/server/proxyServer.groovy"}` to mark server as broken 

:include-file: scenarios/server/proxyServer.groovy {
  title: "mark as broken",
  surroundedBy: "mark-broken",
}

:include-file: scenarios/server/proxyServer.groovy {
  title: "broken server response",
  surroundedBy: "mark-broken-response"
}

# Server Fix

Use `:identifier: fix {validationPath: "scenarios/server/proxyServer.groovy"}` to remove broken and/or slowdown state 

:include-file: scenarios/server/proxyServer.groovy {
  title: "fix server",
  surroundedBy: "mark-fix"
}
