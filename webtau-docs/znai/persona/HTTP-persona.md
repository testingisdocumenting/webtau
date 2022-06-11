# Authorization

In [http headers](HTTP/header#implicit-header) section we defined how to use
an implicit header provider to enable authentication/authorization

:include-file: scenarios/rest/headers/webtau.cfg.groovy {title: "webtau.cfg.groovy"}

:include-file: scenarios/rest/headers/auth/Auth.groovy {title: "scenarios/rest/headers/auth/Auth.groovy"}

# Persona Authorization

Let's define authorization based on persona context.
[Previously](persona/introduction#context-definition) we defined two personas

:include-file: personas/Personas.groovy {
  title: "Alice and Bob"
}

Authorization test using persona concept looks like this

:include-file: scenarios/rest/headers/personaGet.groovy {title: "persona http call"}

To make this example work, let's update implicit header provider above to take into account persona context

:include-file: scenarios/rest/headers/auth/PersonaAuth.groovy {
  title: "scenarios/rest/headers/auth/PersonaAuth.groovy",
  commentsType: "inline"
}
