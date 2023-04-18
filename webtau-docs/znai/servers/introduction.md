WebTau `server` module lets you create and control static, fake and proxy servers:

* Static servers to quickly host HTML, JSON, and similar content 
* Fake servers to control response based request 
* Proxy servers to simulate outages and record interactions for failures investigation

:include-file: scenarios/server/staticContent.groovy {
  title: "static server creation example",
  surroundedBy: "static-server-create",
}

:include-file: scenarios/server/fakeRest.groovy {
  title: "fake server creation example",
  surroundedBy: "router-example"
}

:include-file: scenarios/server/proxyServer.groovy {
  title: "proxy server creation example",
  surroundedBy: "proxy-server-creation",
}

You can apply overrides to any created server. You can also put servers into a "bad" state.

:include-file: scenarios/server/proxyServer.groovy {
  title: "unresponsive server",
  surroundedBy: "mark-unresponsive-example",
}

:include-file: scenarios/server/staticContent.groovy {
  title: "content override",
  surroundedBy: "override-example",
}
