package com.twosigma.webtau.http.datacoverage;

import com.twosigma.webtau.data.traceable.TraceableValue;
import com.twosigma.webtau.http.datanode.DataNodeId;

public interface TraceableValueConverter {
    Object convert(DataNodeId id, TraceableValue traceableValue);
}
