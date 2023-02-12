# Basic Properties

Use assignment to register a config value

:include-file: org/testingisdocumenting/webtau/app/cfg/ConfigParserDslDelegateTest.groovy {
  title: "webtau.cfg.groovy",
  surroundedBy: "basic-properties"
}

# Complex Properties

Multiple ways to register complex properties

:include-file: org/testingisdocumenting/webtau/app/cfg/ConfigParserDslDelegateTest.groovy {
  title: "webtau.cfg.groovy",
  surroundedBy: "complex-properties",
  commentsType: "inline"
}

# Environments

Use `environments` block to define environment specific values and overrides

:include-file: org/testingisdocumenting/webtau/app/cfg/ConfigParserDslDelegateTest.groovy {
  title: "webtau.cfg.groovy",
  surroundedBy: "environment-override"
}

Environment specific complex object overrides 

:include-file: org/testingisdocumenting/webtau/app/cfg/ConfigParserDslDelegateTest.groovy {
  title: "webtau.cfg.groovy",
  surroundedBy: "environment-complex-override"
}

:include-cli-command: webtau --env=dev

# Personas

Use `personas` block to define persona specific values and overrides

:include-file: org/testingisdocumenting/webtau/app/cfg/ConfigParserDslDelegateTest.groovy {
  title: "webtau.cfg.groovy",
  surroundedBy: "persona-overrides"
}

Persona specific complex object overrides 

:include-file: org/testingisdocumenting/webtau/app/cfg/ConfigParserDslDelegateTest.groovy {
  title: "webtau.cfg.groovy",
  surroundedBy: "persona-complex-overrides"
}

# Persona Inside Environment

Use `personas` block inside specific environment within `environments` block to have persona override for an environment 

:include-file: org/testingisdocumenting/webtau/app/cfg/ConfigParserDslDelegateTest.groovy {
  title: "webtau.cfg.groovy",
  surroundedBy: "complex-environment-persona",
  commentsType: "inline"
}

