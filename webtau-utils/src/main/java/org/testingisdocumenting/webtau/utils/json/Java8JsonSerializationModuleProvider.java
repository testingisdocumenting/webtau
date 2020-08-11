package org.testingisdocumenting.webtau.utils.json;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class Java8JsonSerializationModuleProvider implements JsonSerializationModuleProvider {
    @Override
    public Module provide() {
        return new Jdk8Module();
    }
}
