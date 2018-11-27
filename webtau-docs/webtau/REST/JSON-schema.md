# Validation

Webtau supports validation of objects against [JSON Schema](https://json-schema.org/).  For example, to validate the
entire response body use the `complyWithSchema` matcher as follows:

:include-groovy: scenarios/rest/jsonSchema/validateSchema.groovy {bodyOnly: true, title: "Example schema validation"}

The example schemas are:

:include-json: examples/schemas/valid-schema.json {title: "Correct schema for the example above"}
:include-json: examples/schemas/invalid-schema.json {title: "Incorrect schema for the example above"}

The second example above will fail with the following error message:

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

# Configuration

The path to the schema file specified in `complyWithSchema` can be relative or absolute.  If it's relative, it'll be
relative to the `jsonSchemasDir` specified in configuration and if not specified then relative to working directory.
For example:

:include-file: scenarios/rest/jsonSchema/webtau.cfg {title: "Configuration"}
