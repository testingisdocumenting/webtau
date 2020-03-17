package com.twosigma.webtau.schema.expectation;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonMetaSchema;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.ValueMatcher;
import com.twosigma.webtau.http.datacoverage.DataNodeToMapOfValuesConverter;
import com.twosigma.webtau.http.datanode.DataNode;
import com.twosigma.webtau.schema.JsonSchemaConfig;
import com.twosigma.webtau.utils.FileUtils;
import com.twosigma.webtau.utils.JsonUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SchemaMatcher implements ValueMatcher {
    private final String schemaFileName;
    private final JsonSchema schema;

    public SchemaMatcher(String schemaFileName) {
        this.schemaFileName = schemaFileName;

        Path schemaFilePath = JsonSchemaConfig.getSchemasDir().resolve(schemaFileName);

        /*
        This json schema library requires pre-registering different versions.  We'll initialise the builder (which we
        have to do) with the latest version.  This has the side effect of also making that version the default (for
        the case where the schema json does not specify a version).  We'll then register all other versions explicitly
        without changing the default.
         */
        JsonSchemaFactory factory = JsonSchemaFactory
                .builder(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909))
                .addMetaSchema(JsonMetaSchema.getV4())
                .addMetaSchema(JsonMetaSchema.getV6())
                .addMetaSchema(JsonMetaSchema.getV7())
                .build();
        this.schema = factory.getSchema(FileUtils.fileTextContent(schemaFilePath));
    }

    @Override
    public String matchingMessage() {
        return "to comply with schema " + schemaFileName;
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "complies with schema " + schemaFileName;
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return actualPath + " expected to comply with schema " + schemaFileName + "\n" +
                validationsErrors(actual);
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        return validationsErrors(actual).isEmpty();
    }

    private List<String> validationsErrors(Object actual) {
        Object actualObj = actual;
        if (actual instanceof DataNode) {
            DataNodeToMapOfValuesConverter converter = new DataNodeToMapOfValuesConverter((id, traceableValue) ->
                    traceableValue.getValue());

            actualObj = converter.convert((DataNode) actual);
        }

        JsonNode actualJsonObject = JsonUtils.convertToTree(actualObj);
        Set<ValidationMessage> validationMessages = schema.validate(actualJsonObject);
        return validationMessages.stream().map(ValidationMessage::toString).collect(Collectors.toList());
    }

    @Override
    public String negativeMatchingMessage() {
        return "to not comply with schema " + schemaFileName;
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return "does not comply with schema " + schemaFileName;
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return actualPath + " expected to not comply with schema " + schemaFileName + "\n" +
                validationsErrors(actual);
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        return !validationsErrors(actual).isEmpty();
    }

    @Override
    public String toString() {
        return "<comply with " + schemaFileName + ">";
    }
}
