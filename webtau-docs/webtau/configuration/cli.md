# Overrides

Any config file parameter can be overridden with a command line parameter

Given config file

```cfg {title: "test.cfg"}
waitTimeout = 2500
url = http://my-server
```

Values can be overridden using

:include-cli-command: webtau --waitTimeout=25000 --url=http://another-server
