# Dependency

You can use maven to add WebTau as a dependency to you project (for autocompletion or to use with JUnit like runners). 

```tabs
Groovy: :include-file: maven/groovy-dep.xml
Java: :include-file: maven/java-dep.xml
```

# Plugin

Use maven plugin to run pure groovy tests as part of your build. 

:include-file: maven/plugin-wildcard.xml

Use `env` to specify [environment to use](configuration/groovy-config-file#environments). 
Alternatively you can use `url` to override base url.
