# Select

```cfg {title: "test.cfg"}
waitTimeout = 2500
url = http://my-server

environments {
   dev {
       url = "http://localhost:8080"
   }
}
```

:include-cli-command: webtau --env=dev


