# Scenarios

You provide `REST end points` so users can execute various scenarios.
You need to test those scenarios and then document them.

To automate the process let's capture executed scenarios and use them inside your documentation.

# Test Artifacts

To capture artifacts use `http.doc.capture`

:include-file: examples/rest/restPost.groovy {commentsType: "inline"}

`employee-get` directory will be created with request and response json files. By default directory will be created in a
working directory. To change it add `docPath` to your `test.cfg` file 

:include-file: examples/rest/springboot/test.cfg {title: "test.cfg"}

# Document REST calls

If you have user facing scenario tests, capture them and refer inside your documentation.
Set your documentation build pipeline like the one below.  

:include-flow-chart: documentation-flow.json     

Combine REST requests and responses with Open API generated specs for the complete documentation. 
