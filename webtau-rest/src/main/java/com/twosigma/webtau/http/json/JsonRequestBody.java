package com.twosigma.webtau.http.json;

import java.util.Map;

import com.twosigma.webtau.http.HttpRequestBody;
import com.twosigma.webtau.utils.JsonUtils;

public class JsonRequestBody implements HttpRequestBody {
    private final String asString;

    public JsonRequestBody(Map<String, Object> data) {
        this.asString = JsonUtils.serialize(data);
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public String type() {
        return "application/json";
    }

    @Override
    public String asString() {
        return asString;
    }
}
