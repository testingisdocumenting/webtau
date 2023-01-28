# Config File

WebTau let you specify services url, browser settings, DB url connections, etc in a config file.
Depending on [runner](getting-started/installation) you use WebTau will read data from a different place.

```tabs
Groovy:
When you use the Groovy runner, it will look for `webtau.cfg.groovy` file (default). 

:include-file: scenarios/rest/urlOnly.cfg.groovy {
    title: "webtau.cfg.groovy",
    includeRegexp: "localhost"
}

 Note: WebTau treats groovy config file as code

Java:

When you use JUnit like runners, e.g. [JUnit5](getting-started/installation#junit5), WebTau expects file named
`webtau.properties` to be present in test classpath, e.g. test resources:

:include-file: src/test/resources/webtau.properties {
    autoTitle: true,
    includeRegexp: "localhost"
}
```

To change config file location use 

`````tabs
Groovy:
```cli
webtau --config my.conf.groovy
```
Java:
```
-Dwebtau.properties=my.webtau.properties
```
`````

# Environments

WebTau supports environment specific config values, and a way to select which environment to set active during tests run.

```tabs
Groovy:
:include-file: scenarios/rest/urlOnly.cfg.groovy {
    title: "webtau.cfg.groovy",
    excludeRegexp: "package"
}

To select an active environment using [Groovy Standalone Runner](groovy-standalone-runner/introduction) you 
can pass `env` as cli parameter 

:include-cli-command: webtau --env=qa

Additionally you can use environment varialbe to set an active environment: `WEBTAU_ENV=qa`.

Java:
:include-file: webtau-junit5-examples/src/test/resources/webtau.properties {
    title: "src/test/resources/webtau.properties"
}

To select an active environment you have two options:
* System property `-Denv=qa`
* Environment variable `WEBTAU_ENV=qa`
```

# Overrides

WebTau has a list of options you can specify using config file: [url, browserId, etc.](configuration/options).
You can override any value using [environment variables](configuration/options#environment-variable-options):

```
export WEBTAU_URL=http://another-server
export WEBTAU_CHROME_DRIVER_PATH=/path/to/chrome/driver
``` 

In addition to environment variables, you can use a runner specific way to override:
 
## Groovy Standalone Runner

In case of Groovy standalone runner, pass `--<option>=<value>`:

:include-cli-command: webtau --waitTimeout=25000 --url=http://another-server

## JUnit Like Runners

Pass system property via java `-D` option:
```
-Durl=http://another-server
```

# Disable ANSI Colors 

By default, WebTau renders output using colors.  
Use `noColor` option to disable it.

```
export WEBTAU_NO_COLOR=true
-DnoColor=true
webtau --noColor ...
```