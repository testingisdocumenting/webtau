package org.testingisdocumenting.webtau.data.table.header;

import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.testingisdocumenting.webtau.utils.TraceUtils;

import java.util.List;

public class CompositeKeyUnderlyingValueExtractors {
    private static final List<CompositeKeyUnderlyingValueExtractor> extractors = discover();

    public static Object extract(Object value) {
        return extractors.stream()
                .filter(e -> e.handles(value))
                .findFirst().orElseThrow(() -> new IllegalStateException(
                        "No CompositeKeyUnderlyingValueExtractor found for: " + TraceUtils.renderValueAndType(value)))
                .extract(value);
    }

    private static List<CompositeKeyUnderlyingValueExtractor> discover() {
        List<CompositeKeyUnderlyingValueExtractor> result =
                ServiceLoaderUtils.load(CompositeKeyUnderlyingValueExtractor.class);
        result.add(new CompositeKeyDefaultUnderlyingValueExtractor());

        return result;
    }
}
