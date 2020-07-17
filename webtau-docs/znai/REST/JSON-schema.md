# Validation

Webtau supports validation of objects against [JSON Schema](https://json-schema.org/).  It is possible to validate either
the entire body or just a specific field with the `complyWithSchema` matcher as shown in the two examples below:

:include-groovy: doc-artifacts/snippets/json-schema/validateBody.groovy {title: "Validate entire body against JSON schema"}
:include-groovy: doc-artifacts/snippets/json-schema/validateField.groovy {title: "Validate specific field against JSON schema"}

Both examples above validate against the following schema:

:include-json: examples/schemas/valid-schema.json {title: "Correct schema for the example above"}

# Error messages

Using the first example above, an invalid schema will generate an error similar to:
```
invalid schema (examples/scenarios/rest/jsonSchema/validateSchema.groovy)
> executing HTTP GET http://localhost:8080/weather
  X failed expecting body to comply with schema invalid-schema.json : 
      body expected to comply with schema invalid-schema.json
      [#: required key [anotherField] not found, #/temperature: expected type: Boolean, found: Integer]
{
  "temperature": 88
}
```

The schema used in validation to generate this error is as follows:

:include-json: examples/schemas/invalid-schema.json {title: "Incorrect schema for the example above"}

# Configuration

The path to the schema file specified in `complyWithSchema` can be relative or absolute.  If it's relative, it'll be
relative to the `jsonSchemasDir` specified in configuration and if not specified then relative to working directory.
For example:

:include-file: scenarios/rest/jsonSchema/webtau.cfg {title: "Configuration"}
