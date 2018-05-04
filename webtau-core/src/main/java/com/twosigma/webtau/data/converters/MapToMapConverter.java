package com.twosigma.webtau.data.converters;

import com.twosigma.webtau.utils.JavaBeanUtils;

import java.util.Map;

public class MapToMapConverter implements ToMapConverter {
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, ?> convert(Object v) {
        if (v instanceof Map) {
            return (Map<String, ?>) v;
        }

        return null;
    }
}
