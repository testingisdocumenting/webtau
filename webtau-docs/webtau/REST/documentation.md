# Scenarios

You provide `REST endpoints` so users can execute various scenarios.
You need to test those scenarios and then document them.

To automate the process, let's capture executed scenarios and use them inside your documentation.

# Test Artifacts

To capture artifacts use `http.doc.capture`:

:include-file: examples/rest/restPost.groovy {commentsType: "inline"}

An `employee-get` directory will be created with request and response json files. By default, the directory will be 
created in the current working directory. To change it add `docPath` to your `webtau.cfg` file.

:include-file: examples/rest/springboot/webtau.cfg {title: "webtau.cfg"}

# Document REST calls

If you have user facing scenario tests, capture them and refer to them inside your documentation.
Set your documentation build pipeline like below.

:include-flow-chart: documentation-flow.json     

Combine REST requests and responses with Open API generated specs for complete documentation. 
