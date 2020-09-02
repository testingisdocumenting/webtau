package org.testingisdocumenting.webtau.http.datanode;

import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DataNodeIdValidators {
    private static final List<DataNodeIdValidator> globalHandlers = ServiceLoaderUtils.load(DataNodeIdValidator.class);
    private static final ThreadLocal<List<DataNodeIdValidator>> localValidators = ThreadLocal.withInitial(ArrayList::new);

    public static void validate(DataNodeId id) {
        Stream.concat(localValidators.get().stream(), globalHandlers.stream())
                .forEach(validator -> validator.validate(id));
    }

    public static <R> R withAdditionalValidator(DataNodeIdValidator validator, Supplier<R> code) {
        try {
            localValidators.get().add(validator);
            return code.get();
        } finally {
            localValidators.get().remove(validator);
        }
    }

    public static void withAdditionalValidator(DataNodeIdValidator validator, Runnable code) {
        withAdditionalValidator(validator, () -> {
            code.run();
            return null;
        });
    }
}
