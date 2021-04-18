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

package org.testingisdocumenting.webtau.browser.documentation;

import org.testingisdocumenting.webtau.browser.expectation.VisibleValueMatcher;
import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.documentation.DocumentationArtifacts;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.FileUtils;
import org.testingisdocumenting.webtau.utils.JsonUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;
import static java.util.stream.Collectors.toList;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;

public class BrowserDocumentation {
    private final TakesScreenshot screenshotTaker;
    private final List<ImageAnnotation> annotations;
    private final WebDriver driver;

    public BrowserDocumentation(WebDriver driver) {
        this(driver, Collections.emptyList());
    }

    private BrowserDocumentation(WebDriver driver, List<ImageAnnotation> annotations) {
        this.driver = driver;
        this.annotations = annotations;
        this.screenshotTaker = (TakesScreenshot) driver;
    }

    public BrowserDocumentation withAnnotations(ImageAnnotation... annotations) {
        return new BrowserDocumentation(driver, assignDefaultText(Arrays.asList(annotations)));
    }

    public ImageAnnotation badge(PageElement pageElement) {
        return new BadgeImageAnnotation(pageElement, "");
    }

    public ImageAnnotation highlight(PageElement pageElement) {
        return new HighlighterImageAnnotation(pageElement);
    }

    public ImageAnnotation cover(PageElement pageElement) {
        return new RectangleImageAnnotation(pageElement, "");
    }

    public ImageAnnotation cover(PageElement pageElement, String text) {
        return new RectangleImageAnnotation(pageElement, text);
    }

    public ImageAnnotation arrow(PageElement pageElement, String text) {
        return new ArrowImageAnnotation(pageElement, text);
    }

    public void capture(String screenshotName) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(classifier("documentation"), action("capturing"), classifier("screenshot"),
                        AS, urlValue(screenshotName)),
                (path) -> tokenizedMessage(classifier("documentation"), action("captured"), classifier("screenshot")
                        , AS, urlValue(((Path) path).toAbsolutePath())),
                () -> {
                    Path screenshot = createScreenshot(screenshotName);
                    createAnnotations(screenshotName);

                    return screenshot;
                });

        step.execute(StepReportOptions.REPORT_ALL);
    }

    private Path createScreenshot(String screenshotName) {
        DocumentationArtifacts.registerName(screenshotName);

        Screenshot screenshot = new Screenshot(screenshotTaker);

        String artifactName = screenshotName + ".png";

        Path screenShotPath = getCfg().getDocArtifactsPath().resolve(artifactName);
        FileUtils.createDirsForFile(screenShotPath);
        screenshot.save(screenShotPath);

        return screenShotPath;
    }

    private void createAnnotations(String screenshotName) {
        List<? extends Map<String, ?>> shapes = annotations.stream().map(this::createAnnotationData).collect(toList());

        Map<String, Object> result = new HashMap<>();
        result.put("shapes", shapes);
        result.put("pixelRatio", getPixelRatio());

        String annotationsJson = JsonUtils.serializePrettyPrint(result);
        FileUtils.writeTextContent(getCfg().getDocArtifactsPath().resolve(Paths.get(screenshotName + ".json")),
                annotationsJson);
    }

    private Map<String, ?> createAnnotationData(ImageAnnotation annotation) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", annotation.getId());
        data.put("type", annotation.getType());
        data.put("text", annotation.getText());
        data.put("color", annotation.getColor());

        PageElement pageElement = annotation.getPageElement();

        pageElement.should(new VisibleValueMatcher());
        annotation.addAnnotationData(data, pageElement.findElement());

        return data;
    }

    private List<ImageAnnotation> assignDefaultText(List<ImageAnnotation> annotations) {
        int badgeNumber = 0;
        for (ImageAnnotation annotation : annotations) {
            if (annotation instanceof BadgeImageAnnotation) {
                badgeNumber++;
                annotation.setText(String.valueOf(badgeNumber));
            }
        }

        return annotations;
    }

    private Number getPixelRatio() {
        Object pixelRatio = ((JavascriptExecutor) driver).executeScript("return window.devicePixelRatio");
        return pixelRatio instanceof Number ? (Number) pixelRatio : 1;
    }
}
