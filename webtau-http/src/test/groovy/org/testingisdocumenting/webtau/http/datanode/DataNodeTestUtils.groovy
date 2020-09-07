package org.testingisdocumenting.webtau.http.datanode

import org.testingisdocumenting.webtau.data.traceable.CheckLevel

class DataNodeTestUtils {
    static void checkLevelIsRecordedForNonExistentNode(Closure<DataNode> nodeGetter) {
        nodeGetter().traceableValue.checkLevel.should == CheckLevel.None
        nodeGetter().should == null
        nodeGetter().traceableValue.checkLevel.should == CheckLevel.ExplicitPassed
    }
}
