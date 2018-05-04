package com.twosigma.webtau.http;

import com.twosigma.webtau.http.datanode.DataNode;

public class HttpResponseValidatorIgnoringReturn implements HttpResponseValidatorWithReturn {
    private final HttpResponseValidator validator;

    public HttpResponseValidatorIgnoringReturn(HttpResponseValidator validator) {
        this.validator = validator;
    }

    @Override
    public Object validate(HeaderDataNode header, DataNode body) {
        validator.validate(header, body);
        return null;
    }
}
