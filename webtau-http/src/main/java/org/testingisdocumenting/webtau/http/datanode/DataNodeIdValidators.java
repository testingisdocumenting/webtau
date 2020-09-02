package org.testingisdocumenting.webtau.http.datanode;

import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.List;

public class DataNodeIdValidators {
    private static final List<DataNodeIdValidator> validators = ServiceLoaderUtils.load(DataNodeIdValidator.class);

    public static void validate(DataNodeId id) {
        validators.forEach(validator -> validator.validate(id));
    }

    public static void add(DataNodeIdValidator validator) {
        validators.add(validator);
    }

    public static void remove(DataNodeIdValidator validator) {
        validators.remove(validator);
    }
}
