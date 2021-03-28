/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.app.repl


import org.testingisdocumenting.webtau.runner.standalone.StandaloneTest

import java.util.regex.Pattern

class ReplScenariosSelector {
    private final List<StandaloneTest> availableScenarios
    private final List<SelectInputAndResult> inputAndResults

    ReplScenariosSelector(List<StandaloneTest> availableScenarios, Object... selectors) {
        this.availableScenarios = availableScenarios
        this.inputAndResults = selectors.collect { selectScenarios(it) }.flatten()
    }

    List<SelectInputAndResult> getInputAndResults() {
        return inputAndResults
    }

    private List<SelectInputAndResult> selectScenarios(Object selector) {
        if (selector instanceof String) {
            return Collections.singletonList(selectSingleScenarioByRegexp(selector))
        }

        if (selector instanceof Integer) {
            return Collections.singletonList(selectSingleScenarioByIdx(selector))
        }

        if (selector instanceof Map) {
            return selectMultipleScenariosByRange(selector)
        }

        return Collections.singletonList(new SelectInputAndResult(selector, -1, null))
    }
    
    private SelectInputAndResult selectSingleScenarioByRegexp(String regexp) {
        def pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE)
        def idx = availableScenarios.findIndexOf { pattern.matcher(it.scenario) }
        def found = idx == -1 ? null : availableScenarios[idx]

        return new SelectInputAndResult(regexp, idx, found)
    }

    private SelectInputAndResult selectSingleScenarioByIdx(int idx) {
        idx = IndexSelection.convertNegativeIdxToAbsolute(availableScenarios.size(), idx)
        def isInRange = idx >= 0 && idx < availableScenarios.size()
        def found = isInRange ? availableScenarios[idx] : null

        return new SelectInputAndResult(idx, found ? idx : -1, found)
    }

    private List<SelectInputAndResult> selectMultipleScenariosByRange(Map range) {
        if (range.size() != 1) {
            return [new SelectInputAndResult(range, -1, null)]
        }

        def entrySet = range.iterator().next()
        return selectMultipleScenariosByRange(entrySet.key, entrySet.value)
    }

    private List<SelectInputAndResult> selectMultipleScenariosByRange(Object from, Object to) {
        def resultFrom = selectScenarios(from)
        def resultTo = selectScenarios(to)

        if (resultFrom.size() != 1 || resultTo.size() != 1) {
            return [new SelectInputAndResult([(from): to], -1, null)]
        }

        def fromIndex = resultFrom[0].idx
        def toIndex = resultTo[0].idx
        if (fromIndex == -1 || toIndex == -1) {
            return [new SelectInputAndResult([(from): to], -1, null)]
        }

        boolean reversed = fromIndex > toIndex
        int fromAsc = Math.min(fromIndex, toIndex)
        int toAsc = Math.max(fromIndex, toIndex)
        List<SelectInputAndResult> result = []
        for (int idx = fromAsc; idx <= toAsc; idx++) {
            result.add(new SelectInputAndResult(idx, idx, availableScenarios[idx]))
        }

        return reversed ? result.reverse() : result
    }

    static class SelectInputAndResult {
        final Object input
        final boolean isFound
        final int idx
        final String scenario

        SelectInputAndResult(Object input, int idx, StandaloneTest test) {
            this.input = input
            this.isFound = test != null
            this.scenario = test != null ? test.scenario : null
            this.idx = idx
        }

        @Override
        String toString() {
            return "SelectInputAndResult{" +
                    "input=" + input +
                    ", isFound=" + isFound +
                    ", idx=" + idx +
                    ", scenario='" + scenario + '\'' +
                    '}'
        }
    }
}
