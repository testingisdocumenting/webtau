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

import java.util.List;
import java.util.Map;

import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;
import javafx.scene.control.Tab;

import com.twosigma.webtau.data.table.TableData;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.http.datanode.DataNode;

public class DataNodeEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handleNulls() {
        return true;
    }

    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof DataNode;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual,
                        Object expected) {

        Object convertedActual = convertBasedOnExpected((DataNode) actual, expected);
        equalComparator.compare(actualPath, convertedActual, expected);
    }

    private Object convertBasedOnExpected(DataNode actual, Object expected) {
        if (expected instanceof Map) {
            return actual.asMap();
        }

        if (expected instanceof List || expected instanceof TableData) {
            return actual.all();
        }

        return actual.get();
    }
}
