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

package org.testingisdocumenting.webtau.browser.page.value;

import org.testingisdocumenting.webtau.browser.page.WithTokenizedDescription;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.expectation.timer.ExpectationTimer;
import org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.ValueMatcherExpectationSteps;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.OF;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class ElementValue<E, C extends WithTokenizedDescription> implements ActualValueExpectations {
    private final C parent;
    private final String name;
    private final ElementValueFetcher<E> valueFetcher;

    private final TokenizedMessage description;

    public ElementValue(C parent, String name, ElementValueFetcher<E> valueFetcher) {
        this.parent = parent;
        this.name = name;
        this.valueFetcher = valueFetcher;
        this.description = tokenizedMessage(
                IntegrationTestsMessageBuilder.classifier(name)).add(OF).add(parent.describe());
    }

    public C getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public E get() {
        return valueFetcher.fetch();
    }

    public TokenizedMessage describe() {
        return this.description;
    }

    @Override
    public void should(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldStep(this.parent, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher);
    }

    @Override
    public void shouldNot(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldNotStep(this.parent, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher);
    }

    @Override
    public void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ValueMatcherExpectationSteps.waitStep(this.parent, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    @Override
    public void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ValueMatcherExpectationSteps.waitNotStep(this.parent, this, StepReportOptions.REPORT_ALL,
                this.describe(), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }
}
