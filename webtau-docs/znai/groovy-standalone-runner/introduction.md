# Standalone CLI  

Webtau has a standalone runner, so you can author and run tests without needing a build system or IDEs. 
You can fire your editor of choice and start creating automations without worrying about imports and packages
to get your first result.

In Enterprise setups webtau command line tool can be shared with everyone through mount drives or other similar 
mechanisms which makes local testing and CI testing to be much more streamlined.

# Setup 

:include-markdown: installation-groovy-runner.md

:include-cli-command: webtau --example

Navigate into `todo` example

:include-cli-command: cd examples/todo

:include-file: todo/todolist.groovy {title: "todolist.groovy"}

To run test

:include-cli-command: webtau todolist.groovy --url=https://jsonplaceholder.typicode.com {paramsToHighlight: "url"}

# REPL

Webtau standalone runner comes with `repl` mode that let you experiment with API and write tests incrementally.
Repl mode preserves context of the runs which significantly speeds up tests development.

Even if you don't want to use Groovy for your tests, you can still benefit from REPL mode as you experiment with APIs
and system under tests.

[Learn more about REPL](REPL/experiments)     