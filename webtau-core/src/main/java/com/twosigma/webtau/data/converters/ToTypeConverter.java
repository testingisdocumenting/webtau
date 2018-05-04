package com.twosigma.webtau.data.converters;

import java.util.Map;

public interface ToTypeConverter<E> {
    E convert(Object v);
}
