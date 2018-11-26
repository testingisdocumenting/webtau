package com.twosigma.webtau.schema.expectation;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.ValueMatcher;
import com.twosigma.webtau.schema.JsonSchemaConfig;
import com.twosigma.webtau.utils.FileUtils;
import com.twosigma.webtau.utils.JsonUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class SchemaMatcher implements ValueMatcher {
    private final String schemaFileName;
    private final Schema schema;

    public SchemaMatcher(String schemaFileName) {
        this.schemaFileName = schemaFileName;

        Path schemaFilePath = JsonSchemaConfig.getSchemasDir().resolve(schemaFileName);
        JSONObject rawSchema = new JSONObject(new JSONTokener(FileUtils.fileTextContent(schemaFilePath)));
        this.schema = SchemaLoader.load(rawSchema);
    }

    @Override
    public String matchingMessage() {
        return "to match schema " + schemaFileName;
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "matches schema " + schemaFileName;
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return actualPath + " expect to match schema " + schemaFileName + "\n" +
                validationsErrors(actual);
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        return validationsErrors(actual).isEmpty();
    }

    private List<String> validationsErrors(Object actual) {
        JSONObject actualJsonObject = new JSONObject(JsonUtils.serialize(actual));
        try {
            schema.validate(actualJsonObject);
            return Collections.emptyList();
        } catch (ValidationException e) {
            return e.getAllMessages();
        }
    }

    @Override
    public String negativeMatchingMessage() {
        return "to not match schema " + schemaFileName;
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return "does not match schema " + schemaFileName;
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return actualPath + " expect to not match schema " + schemaFileName + "\n" +
                validationsErrors(actual);
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        return !validationsErrors(actual).isEmpty();
    }

    @Override
    public String toString() {
        return "<match " + schemaFileName + ">";
    }
}
