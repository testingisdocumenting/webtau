package com.twosigma.webtau.http;

import com.twosigma.webtau.http.datanode.DataNode;

public interface HttpResponseValidatorWithReturn {
    Object validate(HeaderDataNode header, DataNode body);
}
