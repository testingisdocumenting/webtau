/*
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

package com.twosigma.webtau.reporter.stacktrace;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class StackTraceCodeEntry {
    private final String filePath;
    private Set<Integer> lineNumbers;

    StackTraceCodeEntry(String filePath, Set<Integer> lineNumbers) {
        this.filePath = filePath;
        this.lineNumbers = lineNumbers;
    }

    public String getFilePath() {
        return filePath;
    }

    public Set<Integer> getLineNumbers() {
        return lineNumbers;
    }

    public void addLineNumbers(Set<Integer> lineNumbers) {
        Set<Integer> newNumbers = new TreeSet<>(this.lineNumbers);
        newNumbers.addAll(lineNumbers);

        this.lineNumbers = newNumbers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StackTraceCodeEntry that = (StackTraceCodeEntry) o;

        return Objects.equals(filePath, that.filePath) &&
                Objects.equals(lineNumbers, that.lineNumbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, lineNumbers);
    }
}
