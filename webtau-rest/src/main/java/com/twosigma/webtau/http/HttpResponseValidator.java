package com.twosigma.webtau.http;

import com.twosigma.webtau.http.datanode.DataNode;

public interface HttpResponseValidator {
    void validate(HeaderDataNode header, DataNode body);
}
