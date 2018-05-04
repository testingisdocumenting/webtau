package com.twosigma.webtau.data.converters;

import com.twosigma.webtau.utils.ServiceLoaderUtils;

import java.util.Set;

public class ToNumberConverters {
    private static Set<ToNumberConverter> converters = ServiceLoaderUtils.load(ToNumberConverter.class);

    public static Number convert(Object v) {
        if (v instanceof Number) {
            return (Number) v;
        }

        return TypeConvertersUtils.convert(converters.stream(), "number", v);
    }
}
