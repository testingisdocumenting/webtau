# Implicit Page Open Logic

You can register a custom callback that will be called implicitly every time a web page is opened. 
One of the typical use cases is to inject cookies or values into a local storage before tests logic begins. 
 
# Auto Login

To register an open handler you need to define `browserPageNavigationHandlerProvider` config value.

:include-file: examples/scenarios/ui/openHandler.cfg.groovy {title: "webtau.cfg.groovy"}
 
:include-file: examples/scenarios/ui/openHandler.groovy {title: "Handler will be called before first assertion"}

Note: usage of `localStorage` is just an example. You can use `cookies`, call external services using `http.` or load
credentials from a file system. 


