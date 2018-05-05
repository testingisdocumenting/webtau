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

package com.twosigma.webtau.expectation.contain.handlers;

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.contain.ContainAnalyzer;
import com.twosigma.webtau.expectation.contain.ContainHandler;
import com.twosigma.webtau.expectation.equality.ComparatorResult;
import com.twosigma.webtau.expectation.equality.EqualComparator;

import java.util.Iterator;
import java.util.function.BiFunction;

public class IterableContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof Iterable;
    }

    @Override
    public void analyzeContain(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        EqualComparator equalComparator = EqualComparator.comparator();

        boolean reachedEnd = forEachActual(actualPath, actual,
                ((indexedPath, actualValue) -> {
                    ComparatorResult comparatorResult = equalComparator.compare(indexedPath, actualValue, expected);
                    return !comparatorResult.isMismatch();
                }));

        if (! reachedEnd) {
            containAnalyzer.reportMismatch(this, actualPath, equalComparator.generateMismatchReport());
        }
    }

    @Override
    public void analyzeNotContain(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        EqualComparator equalComparator = EqualComparator.comparator();

        forEachActual(actualPath, actual, ((indexedPath, actualValue) -> {
            ComparatorResult comparatorResult = equalComparator.compare(indexedPath, actualValue, expected);

            if (!comparatorResult.isMismatch()) {
                containAnalyzer.reportMismatch(this, indexedPath,
                        "equals " + DataRenderers.render(actualValue));
            }

            return false;
        }));
    }

    private boolean forEachActual(ActualPath actualPath,
                                  Object actual,
                                  BiFunction<ActualPath, Object, Boolean> function) {
        Iterable actualIterable = (Iterable) actual;
        Iterator iterator = actualIterable.iterator();

        int idx = 0;
        while (iterator.hasNext()) {
            Object v = iterator.next();
            ActualPath indexedPath = actualPath.index(idx);
            Boolean terminate = function.apply(indexedPath, v);
            if (terminate) {
                return true;
            }

            idx++;
        }

        return false;
    }
}
