# Authorization

In [http headers](HTTP/header#implicit-header) section we defined how to use
an implicit header provider to enable authentication/authorization

```tabs
Groovy:
:include-file: scenarios/rest/headers/webtau.cfg.groovy {title: "webtau.cfg.groovy"}

:include-file: scenarios/rest/headers/auth/Auth.groovy {title: "scenarios/rest/headers/auth/Auth.groovy"}

Java:
In case of JUnit like runners, WebTau uses [Service Loaders](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html) 
to locate header providers

:include-file: src/test/resources/META-INF/services/org.testingisdocumenting.webtau.http.config.WebTauHttpConfiguration {
  autoTitle: true
}

:include-file: src/test/java/com/example/tests/junit5/config/HttpAuthHeaderProvider.java {
  autoTitle: true
}
```

# Persona Authorization

Let's define authorization based on persona context.
[Previously](persona/introduction#context-definition) we defined two personas

```tabs
Groovy:
:include-file: personas/Personas.groovy {
  title: "Alice and Bob"
}

Java:
:include-file: com/example/tests/junit5/Personas.java {
  title: "Alice and Bob"
}
```

Authorization test using persona concept looks like this

```tabs
Groovy:
:include-file: scenarios/rest/headers/personaGet.groovy {title: "persona http call"}

Java:
:include-file: com/example/tests/junit5/PersonaHttpJavaTest.java {title: "persona http call"}
```

To make this example work, let's update implicit header provider above to take into account persona context

```tabs
Groovy:
:include-file: scenarios/rest/headers/auth/PersonaAuth.groovy {
   autoTitle: true,
   commentsType: "inline"
}

Java:
:include-file: src/test/java/com/example/tests/junit5/config/HttpPersonaAuthHeaderProvider.java {
   autoTitle: true,
   commentsType: "inline"
}
```
