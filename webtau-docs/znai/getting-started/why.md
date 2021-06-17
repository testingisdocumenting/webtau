# Consistent API

There are multiple tools to help you test REST API, GraphQL API, Web UI, Database and CLI. 
Some of them are JUnit specific, some of them are UI driven. Some use their own matchers and some try to integrate with 
a specific matcher interface.

Question: Why did we create another tool? 

Tests I write, and the way I write them often require interactions and assertions on multiple layers.
Combining multiple frameworks to achieve the goal ends up with a mix of styles in a single test file. 
It complicates tests creation and maintenance.

# Reporting

Webtau in its core captures test actions and assertions. 
Everything you do, every match that is **passed** or failed is being recorded.

Information is available in console output, so you don't need to sprinkle `println` statements everywhere. 
And all the captured information is available as self-contained rich HTML report.  

# Syntax Sugar

Webtau provides DSL to make common testing operations succinct. Syntax sugar is available for Java and Groovy and 
since webtau core is Java, additional syntax sugar can be added to languages like Kotlin and Scala.  

# REPL

Writing end-to-end tests is hard and there are a lot of excuses not write one. 
End to end test feedback loop is usually long and slow. Webtau provides [REPL](REPL/experiments) mode to help you 
experiment with API and write a test in incremental fashion. 

# Utilities

Webtau provides many utility functions to simplify data organization and setup. [Data](utilities/data) module provides
shortcuts to deal with `CSV` and `JSON` based data. [File System](utilities/file-system) module provides shortcuts to 
deal with file system related things.

# Documentation Artifacts

I believe that big chunks of a documentation of your product should be automatically generated:
* Screenshots (with annotations)
* API request/response examples
* CLI params and sample output

Webtau provides a set of commands to help you capture artifacts of your happy path tests to later be used  
by a documentation system. 

Captured artifacts are agnostic to the documentation system. I personally use [Znai](https://github.com/testingisdocumenting/znai),
a markdown based documentation with custom extensions (I am a maintainer of the project). 
