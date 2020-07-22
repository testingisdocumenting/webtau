# Overrides

Any config file parameter can be overridden with a command line parameter.

For example, given this config file:

```cfg {title: "webtau.cfg"}
waitTimeout = 2500
url = http://my-server
```

Values can be overridden as follows:

:include-cli-command: webtau --waitTimeout=25000 --url=http://another-server
