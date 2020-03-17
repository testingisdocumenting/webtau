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

package org.testingisdocumenting.webtau.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CliOutputListener {
    private List<PredicateWithHandler> lineHandlers;

    public CliOutputListener() {
        this.lineHandlers = new ArrayList<>();
    }

    public void on(String partialLine, Runnable handler) {
        lineHandlers.add(new PredicateWithHandler(new PartialLineMatch(partialLine), handler));
    }

    public Stream<PredicateWithHandler> lineHandlersStream() {
        return lineHandlers.stream();
    }

    private static class PredicateWithHandler {
        Predicate<String> predicate;
        Runnable handler;

        public PredicateWithHandler(Predicate<String> predicate, Runnable handler) {
            this.predicate = predicate;
            this.handler = handler;
        }
    }

    private static class PartialLineMatch implements Predicate<String> {
        private final String partialExpectation;

        public PartialLineMatch(String partialExpectation) {
            this.partialExpectation = partialExpectation;
        }

        @Override
        public boolean test(String line) {
            return line.contains(partialExpectation);
        }
    }
}
