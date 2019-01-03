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

package com.twosigma.webtau.browser.page.value;

import com.twosigma.webtau.browser.page.WithTokenizedDescription;
import com.twosigma.webtau.expectation.ActualValueExpectations;
import com.twosigma.webtau.expectation.ValueMatcher;
import com.twosigma.webtau.expectation.timer.ExpectationTimer;
import com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder;
import com.twosigma.webtau.reporter.StepReportOptions;
import com.twosigma.webtau.reporter.TokenizedMessage;
import com.twosigma.webtau.reporter.ValueMatcherExpectationSteps;

import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.OF;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class ElementValue<E, C extends WithTokenizedDescription> implements ActualValueExpectations {
    private C parent;
    private String name;
    private ElementValueFetcher<E> valueFetcher;

    private TokenizedMessage description;

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
