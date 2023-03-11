/*
 * Copyright 2020 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.webtau.http.datanode;

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.BinaryDataProvider;
import org.testingisdocumenting.webtau.data.render.*;
import org.testingisdocumenting.webtau.data.traceable.TraceableValue;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToResult;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.WebTauCore.createActualPath;

public interface DataNode extends DataNodeExpectations, BinaryDataProvider, Comparable<Object>, Iterable<DataNode>, PrettyPrintable {
    DataNodeId id();

    DataNode get(String pathOrName);

    boolean has(String pathOrName);

    DataNode get(int idx);

    TraceableValue getTraceableValue();

    <E> E get();

    boolean isList();

    boolean isSingleValue();

    List<DataNode> elements();

    Collection<DataNode> children();

    DataNode find(Predicate<DataNode> predicate);

    DataNode findAll(Predicate<DataNode> predicate);

    int numberOfChildren();

    int numberOfElements();

    @Override
    default byte[] getBinaryContent() {
        if (!isBinary()) {
            throw new IllegalArgumentException("datanode is not binary");
        }

        return get();
    }

    @Override
    default String binaryDataSource() {
        return id().getName();
    }

    default boolean isNull() {
        return false;
    }

    default boolean isBinary() {
        return getTraceableValue() != null &&
                getTraceableValue().getValue() != null &&
                getTraceableValue().getValue().getClass().equals(byte[].class);
    }

    @Override
    default ValuePath actualPath() {
        return createActualPath(id().getPath());
    }

    @Override
    default int compareTo(Object rhv) {
        CompareToComparator comparator = CompareToComparator.comparator(
                CompareToComparator.AssertionMode.GREATER_THAN);

        CompareToResult compareToResult = TraceableValue.withAlwaysFuzzyPassedChecks(
                () -> comparator.compareUsingCompareTo(actualPath(), this, rhv));

        if (compareToResult.isGreater()) {
            return 1;
        } else if (compareToResult.isLess()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    default void prettyPrint(PrettyPrinter printer) {
        prettyPrint(printer, ValuePath.UNDEFINED, RenderMethod.REGULAR, PrettyPrinterDecorationToken.EMPTY);
    }

    @Override
    default void prettyPrint(PrettyPrinter printer, ValuePath rootPath, RenderMethod renderMethod, PrettyPrinterDecorationToken decorationToken) {
        if (isList()) {
            IterablePrettyPrintable printable = new IterablePrettyPrintable(elements());
            printable.prettyPrint(printer, rootPath, renderMethod, decorationToken);
        } else if (isSingleValue()) {
            printer.printObject(getTraceableValue());
        } else if (isNull()) {
            printer.printObject(null);
        } else if (isBinary()) {
            printer.printLine(Color.YELLOW, "[binary content]");
        } else {
            Map<String, DataNode> childrenAsMap = children().stream()
                    .collect(Collectors.toMap(node -> node.id().getName(), node -> node, (l, r) -> r, LinkedHashMap::new));

            MapPrettyPrintable printable = new MapPrettyPrintable(childrenAsMap);
            printable.prettyPrint(printer, rootPath, renderMethod, decorationToken);
        }
    }
}
