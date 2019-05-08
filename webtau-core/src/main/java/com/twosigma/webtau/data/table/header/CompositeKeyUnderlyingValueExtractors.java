package com.twosigma.webtau.data.table.header;

import com.twosigma.webtau.utils.ServiceLoaderUtils;
import com.twosigma.webtau.utils.TraceUtils;

import java.util.LinkedHashSet;
import java.util.Set;

public class CompositeKeyUnderlyingValueExtractors {
    private static final Set<CompositeKeyUnderlyingValueExtractor> extractors = discover();

    public static Object extract(Object value) {
        return extractors.stream()
                .filter(e -> e.handles(value))
                .findFirst().orElseThrow(() -> new IllegalStateException(
                        "No CompositeKeyUnderlyingValueExtractor found for: " + TraceUtils.renderValueAndType(value)))
                .extract(value);
    }

    private static Set<CompositeKeyUnderlyingValueExtractor> discover() {
        Set<CompositeKeyUnderlyingValueExtractor> result =
                new LinkedHashSet<>(ServiceLoaderUtils.load(CompositeKeyUnderlyingValueExtractor.class));
        result.add(new CompositeKeyDefaultUnderlyingValueExtractor());

        return result;
    }
}
