package com.twosigma.webtau.data.converters;

import com.twosigma.webtau.utils.JavaBeanUtils;

import java.util.Map;

public class BeanToMapConverter implements ToMapConverter {
    @Override
    public Map<String, ?> convert(Object v) {
        return JavaBeanUtils.convertBeanToMap(v);
    }
}
