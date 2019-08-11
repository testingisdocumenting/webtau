Generate webtau examples 

:include-cli-command: webtau --example

Navigate into `todo` example

:include-cli-command: cd examples/todo

:include-file: examples/todo/todolist.groovy {title: "todolist.groovy"}

To run test

:include-cli-command: webtau todolist.groovy --url=https://jsonplaceholder.typicode.com {paramsToHighlight: "url"}
