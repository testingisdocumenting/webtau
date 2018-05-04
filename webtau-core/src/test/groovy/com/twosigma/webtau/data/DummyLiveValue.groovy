package com.twosigma.webtau.data

import com.twosigma.webtau.data.live.LiveValue

class DummyLiveValue implements LiveValue {
    int index
    List<?> values

    DummyLiveValue(List<?> values) {
        this.values = values
    }

    @Override
    Object get() {
        return values[index++]
    }
}
