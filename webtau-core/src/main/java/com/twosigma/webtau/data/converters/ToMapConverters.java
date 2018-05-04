package com.twosigma.webtau.data.converters;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.twosigma.webtau.utils.ServiceUtils;
import com.twosigma.webtau.utils.TraceUtils;

public class ToMapConverters {
    private static List<ToMapConverter> converters = discover();

    public static Map<String, ?> convert(Object v) {
        if (v == null) {
            return null;
        }

        return converters.stream().
                map(h -> h.convert(v)).
                filter(Objects::nonNull).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException("can't find a map converter for: " + TraceUtils.renderValueAndType(v)));
    }

    private static List<ToMapConverter> discover() {
        List<ToMapConverter> discovered = ServiceUtils.discover(ToMapConverter.class);
        discovered.add(new MapToMapConverter());
        discovered.add(new BeanToMapConverter());

        return discovered;
    }
}
