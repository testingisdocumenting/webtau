package com.twosigma.webtau.schema;

import com.twosigma.webtau.cfg.ConfigValue;
import com.twosigma.webtau.cfg.WebTauConfigHandler;

import java.nio.file.Path;
import java.util.stream.Stream;

import static com.twosigma.webtau.cfg.ConfigValue.declare;
import static com.twosigma.webtau.cfg.WebTauConfig.getCfg;

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
