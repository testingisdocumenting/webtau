package com.twosigma.webtau.reporter;

import com.twosigma.webtau.utils.CollectionUtils;

import java.util.Map;

public class MessageToken {
    private String type;
    private Object value;

    public MessageToken(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public Map<String, ?> toMap() {
        return CollectionUtils.createMap("type", type, "value", value);
    }

    @Override
    public String toString() {
        return "MessageToken{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
