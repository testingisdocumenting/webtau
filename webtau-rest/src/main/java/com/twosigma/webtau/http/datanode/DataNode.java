package com.twosigma.webtau.http.datanode;

import com.twosigma.webtau.data.traceable.TraceableValue;
import com.twosigma.webtau.expectation.*;

import java.util.List;
import java.util.Map;

import static com.twosigma.webtau.Ddjt.createActualPath;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public interface DataNode extends ActualValueExpectations, DataNodeExpectations {
    DataNodeId id();

    DataNode get(String name);

    DataNode get(int idx);

    TraceableValue get();

    boolean isList();

    boolean isSingleValue();

    List<DataNode> all();

    int numberOfChildren();

    int numberOfElements();

    Map<String, DataNode> asMap();

    @Override
    default ActualPath actualPath() {
        return createActualPath(id().getPath());
    }
}
