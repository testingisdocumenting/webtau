# Environment Variables

Each persona can have their own `cli.run` environment variable overrides 

Common variables definition:

:include-file: scenarios/cli/webtau-cli-env-vars.cfg.groovy {
  title: "webtau.cfg.groovy",
  surroundedBy: "default-my-var"
}

Persona overrides

:include-file: scenarios/cli/webtau-cli-env-vars.cfg.groovy {
  title: "webtau.cfg.groovy",
  surroundedBy: "personas-my-var"
}

Script that prints env variable value 

:include-file: scripts/hello-env-var {autoTitle: true}

Different value will be printed depending on the active persona

:include-file: doc-artifacts/snippets/common-env-vars/personaOverridesForeground.groovy { 
  title: "use environment variable from persona config"
}
