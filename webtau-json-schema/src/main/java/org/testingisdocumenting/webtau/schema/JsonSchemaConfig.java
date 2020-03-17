package org.testingisdocumenting.webtau.schema;

import org.testingisdocumenting.webtau.cfg.ConfigValue;
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler;

import java.nio.file.Path;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.cfg.ConfigValue.declare;
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;

public class JsonSchemaConfig implements WebTauConfigHandler {
    private static final ConfigValue schemasDir = declare("jsonSchemasDir",
            "url of directory containing JSON schemas", () -> getCfg().getWorkingDir());

    public static Path getSchemasDir() {
        Path schemasDirPath = schemasDir.getAsPath();
        if (schemasDirPath.isAbsolute()) {
            return schemasDirPath;
        }

        return getCfg().getWorkingDir().resolve(schemasDirPath);
    }

    @Override
    public Stream<ConfigValue> additionalConfigValues() {
        return Stream.of(schemasDir);
    }
}
