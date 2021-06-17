# Serial execution

The default mode for running tests is serially; in other words, scenario files are executed one after the other.

# Parallel execution

Webtau supports executing tests in parallel.  In this mode, **scenario files** are executed in parallel.  **Individual 
scenarios** are still executed sequentially.

For large test suites, it is therefore advisable to create many small focused scenario files instead of few large files.

To enable parallel execution, specify the `numberOfThreads` configuration property either through the configuration file
or as a CLI parameter.  This property dictates the maximum number of threads on which to run tests.  Alternatively,
set `numberOfThreads` to `-1` and webtau will use as many threads as there are scenario files.

Note: scenario file execution order is not guaranteed.

# Scenario discovery

Webtau via CLI or Maven plugin supports a number of methods for specifying and discovering scenarios.

The simplest way is to list the scenario files explicitly:

```tabs
CLI: :include-cli-command: webtau scenarios/rest/simpleGet.groovy scnearios/rest/simplePost.groovy
Maven: :include-file: maven/plugin-list.xml
```

Wildcard matching is also supported.  In the CLI version this is normal shell [glob](https://en.wikipedia.org/wiki/Glob_\(programming\))
and in Maven it's a standard Maven file inclusion block:

```tabs
CLI: :include-cli-command: webtau scenarios/rest/simple*.groovy
Maven: :include-file: maven/plugin-wildcard.xml
```

It is also possible to include a set of base directories and webtau will then find all `*.groovy` files within
them, recursively:

```tabs
CLI: :include-cli-command: webtau scenarios/rest
Maven: :include-file: maven/plugin-discover.xml
```

In this mode, the HTML report that webtau generates will show paths to the files relative to the
requested directories.
