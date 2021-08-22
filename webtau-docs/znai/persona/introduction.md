# Persona Context

Webtau `persona` defines a user of your system with a context associated with that user.
Example of context: authentication credentials, browser, custom config parameters.

:include-file: scenarios/concept/personaContext.groovy {
  title: "context example",
  surroundedBy: "context-example-snippet"
}

Alice and Bob execute the same action. Within the action we can access who 
is the current persona and what is the `payload` of the persona. 

:include-groovy: scenarios/concept/personaContext.groovy {
  title: "same action performed by two personas",
  entry: "customAction",
  commentsType: "inline"
}

# Context Definition

Persona is defined with `persona`. 
Persona can be created in place or in centralized place

:include-file: personas/Personas.groovy {
  title: "centralized persona definition with payload"
}

Per persona config section let you define or override any config value, including
timeouts, browser preferences, etc

:include-file: scenarios/concept/webtau.persona.cfg.groovy {
  title: "persona specific config",
  startLine: "personas-cfg",
  endLine: "personas-cfg",
  excludeStartEnd: true
}

Snippet from above example to show how to access config values and payload

:include-groovy: scenarios/concept/personaContext.groovy {
  title: "access payload and config example",
  entry: "customAction",
  commentsType: "inline",
  includeRegexp: ["def authId", "def email"]
}

# Report

Report captures what step was performed by what persona

:include-image: doc-artifacts/reports/persona-concept-steps.png {fit: true}

