/*
 * Copyright 2023 webtau maintainers
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

import org.testingisdocumenting.webtau.browser.table.BrowserTableParser;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.expectation.state.VisibleStateAware;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;

import java.util.List;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class TablePageElement implements
        ActualValueExpectations,
        PrettyPrintable,
        ActualPathAndDescriptionAware,
        VisibleStateAware {
    private final PageElement root;

    public final PageElementValue<TableData> value;

    public TablePageElement(PageElement root) {
        this.root = root;
        this.value = new PageElementValue<>(this, "value", this::extractTableData);
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        root.prettyPrint(printer);
    }

    @Override
    public ValuePath actualPath() {
        return createActualPath("pageElement");
    }

    @Override
    public boolean isVisible() {
        return root.isVisible();
    }

    public TableData extractTableData() {
        List<HtmlNode> htmlNodes = root.extractHtmlNodes();
        if (htmlNodes.isEmpty()) {
            return new TableData(Stream.empty());
        }

        return BrowserTableParser.parse(htmlNodes.stream().findFirst().get().outerHtml());
    }

    @Override
    public StepReportOptions shouldReportOption() {
        return StepReportOptions.REPORT_ALL;
    }
}
