package com.twosigma.webtau.data.converters

class DummyNumberToMapConverter implements ToMapConverter {
    @Override
    Map<String, ?> convert(Object v) {
        return v instanceof Number ? Collections.singletonMap("DummyNumberToMapConverter", v) : null
    }
}
