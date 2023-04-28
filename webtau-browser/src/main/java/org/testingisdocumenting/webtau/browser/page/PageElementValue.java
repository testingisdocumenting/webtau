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

package org.testingisdocumenting.webtau.browser.page;

import org.testingisdocumenting.webtau.browser.Browser;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.DataRenderers;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.TokenizedMessageToAnsiConverter;

import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;

/**
 * Live element value that can be matched or waited against
 * @param <E> element value type
 */
public class PageElementValue<E> implements ActualValueExpectations, ActualPathAndDescriptionAware, PrettyPrintable {
    private final ActualPathAndDescriptionAware parent;
    private final String name;
    private final PageElementValueFetcher<E> valueFetcher;

    private final TokenizedMessage description;

    public PageElementValue(ActualPathAndDescriptionAware parent, String name, PageElementValueFetcher<E> valueFetcher) {
        this.parent = parent;
        this.name = name;
        this.valueFetcher = valueFetcher;
        this.description = tokenizedMessage().classifier(name).of().add(parent.describe());
    }

    public ActualPathAndDescriptionAware getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public E get() {
        return valueFetcher.fetch();
    }

    @Override
    public ValuePath actualPath() {
        return createActualPath("pageElementValue");
    }

    @Override
    public TokenizedMessage describe() {
        return this.description;
    }

    @Override
    public StepReportOptions shouldReportOption() {
        return StepReportOptions.REPORT_ALL;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        // to avoid browser recreation if it was already closed
        if (!Browser.browser.hasActiveBrowsers()) {
            return;
        }

        printer.printLine(
                Stream.concat(parentPrettyPrint(),
                        Stream.of(Color.PURPLE, name, ":", Color.GREEN, " ", DataRenderers.render(get()))).toArray());
    }

    private Stream<Object> parentPrettyPrint() {
        if (parent == null) {
            return Stream.empty();
        }

        return Stream.concat(TokenizedMessageToAnsiConverter.DEFAULT.convert(ValueConverter.EMPTY, parent.describe(), 0).stream(), Stream.of(" "));
    }
}
