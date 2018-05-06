/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.webtau.http.datanode;

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.data.traceable.CheckLevel;
import com.twosigma.webtau.data.traceable.TraceableValue;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.contain.ContainAnalyzer;
import com.twosigma.webtau.expectation.contain.ContainHandler;
import com.twosigma.webtau.expectation.contain.handlers.IndexedValue;
import com.twosigma.webtau.expectation.contain.handlers.IterableContainAnalyzer;
import com.twosigma.webtau.expectation.equality.EqualComparator;

import java.util.List;
import java.util.stream.Collectors;

public class DataNodeListContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof DataNode && ((DataNode) actual).isList();
    }

    @Override
    public void analyzeContain(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        List<DataNode> dataNodes = getDataNodes(actual);
        IterableContainAnalyzer analyzer = new IterableContainAnalyzer(actualPath, dataNodes, expected);
        List<IndexedValue> indexedValues = TraceableValue.withDisabledChecks(analyzer::containingIndexedValues);

        if (indexedValues.isEmpty()) {
            containAnalyzer.reportMismatch(this, actualPath, analyzer.getEqualComparator()
                    .generateMismatchReport());

            dataNodes.forEach(n -> n.get().updateCheckLevel(CheckLevel.ExplicitFailed));
        } else {
            // earlier traceable value is disabled and indexes of matches are found
            // it is done to avoid marking every mismatching entry as failed
            // now for found entries we simulate comparison again but this time values will be properly marked as matched
            EqualComparator equalComparator = EqualComparator.comparator();
            indexedValues.forEach(iv -> equalComparator.compare(actualPath, dataNodes.get(iv.getIdx()), expected));
        }
    }

    @Override
    public void analyzeNotContain(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        List<DataNode> dataNodes = getDataNodes(actual);
        IterableContainAnalyzer analyzer = new IterableContainAnalyzer(actualPath, dataNodes, expected);
        List<IndexedValue> indexedValues = TraceableValue.withDisabledChecks(analyzer::containingIndexedValues);

        if (indexedValues.isEmpty()) {
            dataNodes.forEach(n -> n.get().updateCheckLevel(CheckLevel.FuzzyPassed));
        } else {
            EqualComparator equalComparator = EqualComparator.negativeComparator();

//            TraceableValue.withModifier(CheckLevel::reverse, () ->
                    indexedValues.forEach(indexedValue -> {
                        ActualPath indexedPath = actualPath.index(indexedValue.getIdx());

                        containAnalyzer.reportMismatch(this, indexedPath,
                                "equals " + DataRenderers.render(indexedValue.getValue()));
                        equalComparator.compare(indexedPath, dataNodes.get(indexedValue.getIdx()), expected);
                    });
        }
    }

    private List<DataNode> getDataNodes(Object actual) {
        DataNode listNode = (DataNode) actual;
        return listNode.all();
    }
}
