<#list names as name>
:include-template: templates/method-layout.md {name: "${name}", paramsPath: "test-artifacts/api/${name}.groovy.result"}
</#list>