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

package org.testingisdocumenting.webtau.browser;

import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.testingisdocumenting.webtau.browser.page.PageElementValue;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.reporter.WebTauStepOutputKeyValue;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class BrowserAlert implements ActualPathAndDescriptionAware {
    private final WebDriver driver;
    public final PageElementValue<String> text;

    public BrowserAlert(WebDriver driver) {
        this.driver = driver;
        this.text = new PageElementValue<>(this, "text", this::extractText);
    }

    public void dismiss() {
        WebTauStep step = WebTauStep.createStep(tokenizedMessage().action("dismissing").classifier("alert"),
                () -> tokenizedMessage().action("dismissed").classifier("alert"),
                () -> {
                    var alert = driver.switchTo().alert();
                    String text = alert.getText();
                    alert.dismiss();

                    return text;
                });

        step.setStepOutputFunc((text) -> WebTauStepOutputKeyValue.stepOutput("dismissed alert text", text));
        step.execute(StepReportOptions.REPORT_ALL);
    }

    public void accept() {
        WebTauStep step = WebTauStep.createStep(tokenizedMessage().action("accepting").classifier("alert"),
                () -> tokenizedMessage().action("accepted").classifier("alert"),
                () -> {
                    var alert = driver.switchTo().alert();
                    String text = alert.getText();
                    alert.accept();

                    return text;
                });

        step.setStepOutputFunc((text) -> WebTauStepOutputKeyValue.stepOutput("accepted alert text", text));
        step.execute(StepReportOptions.REPORT_ALL);
    }

    private String extractText() {
        try {
            return driver.switchTo().alert().getText();
        } catch (NoAlertPresentException e) {
            return null;
        }
    }

    @Override
    public ValuePath actualPath() {
        return new ValuePath("browser alert");
    }
}
