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

import org.testingisdocumenting.webtau.browser.BrowserContext;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class PageUrl implements PrettyPrintable, ActualValueExpectations, ActualPathAndDescriptionAware {
    private static final BrowserContext browserContext = BrowserContext.INSTANCE;

  private final Supplier<String> currentUrlSupplier;

    public PageUrl(Supplier<String> currentUrlSupplier) {
        this.currentUrlSupplier = currentUrlSupplier;
    }

    public final PageElementValue<String> full =
            new PageElementValue<>(browserContext, "full page url", this::fetchUrl);

    public final PageElementValue<String> path =
            new PageElementValue<>(browserContext, "page url path", this::fetchPath);

    public final PageElementValue<String> query =
            new PageElementValue<>(browserContext, "page url query", this::fetchQuery);

    public final PageElementValue<String> ref =
            new PageElementValue<>(browserContext, "page url ref", this::fetchRef);

    public String get() {
        return fetchUrl();
    }

    private String fetchUrl() {
        return emptyAsNull(currentUrlSupplier.get());
    }

    private String fetchPath() {
        return emptyAsNull(fetchAsUrl().getPath());
    }

    private String fetchQuery() {
        return emptyAsNull(fetchAsUrl().getQuery());
    }

    private String fetchRef() {
        return emptyAsNull(fetchAsUrl().getRef());
    }

    private String emptyAsNull(String value) {
        return value == null ? "" : value;
    }

    private URL fetchAsUrl() {
        try {
            return new URL(fetchUrl());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return PrettyPrinter.renderAsTextWithoutColors(this);
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        printer.printLine(Color.YELLOW, " full: ", Color.GREEN, full.get());
        printer.printLine(Color.YELLOW, " path: ", Color.GREEN, path.get());
        printer.printLine(Color.YELLOW, "query: ", Color.GREEN, query.get());
        printer.printLine(Color.YELLOW, "  ref: ", Color.GREEN, ref.get());
    }

    @Override
    public ValuePath actualPath() {
        return createActualPath("url");
    }

    @Override
    public StepReportOptions shouldReportOption() {
        return StepReportOptions.REPORT_ALL;
    }
}
