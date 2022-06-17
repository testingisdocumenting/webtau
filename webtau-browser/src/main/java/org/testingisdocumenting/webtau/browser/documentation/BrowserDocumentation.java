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

import org.openqa.selenium.*;
import org.testingisdocumenting.webtau.browser.expectation.VisibleValueMatcher;
import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.documentation.DocumentationArtifacts;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.FileUtils;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.*;
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.*;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;

public class BrowserDocumentation {
    private final List<ImageAnnotation> annotations;

    /**
     * capture limits screenshot to this optional element
     */
    private PageElement rootElement;

    private final WebDriver driver;

    public BrowserDocumentation(WebDriver driver) {
        this(driver, Collections.emptyList());
    }

    private BrowserDocumentation(WebDriver driver, List<ImageAnnotation> annotations) {
        this.driver = driver;
        this.annotations = annotations;
    }

    public BrowserDocumentation withRoot(PageElement pageElement) {
        BrowserDocumentation browserDocumentation = new BrowserDocumentation(driver, new ArrayList<>(annotations));
        browserDocumentation.rootElement = pageElement;

        return browserDocumentation;
    }

    public BrowserDocumentation withAnnotations(PageElement... pageElements) {
        return withAnnotations(Arrays.stream(pageElements)
                .map(this::badge)
                .toArray(ImageAnnotation[]::new));
    }

    public BrowserDocumentation withAnnotations(ImageAnnotation... annotations) {
        BrowserDocumentation browserDocumentation = new BrowserDocumentation(driver, assignDefaultText(Arrays.asList(annotations)));
        browserDocumentation.rootElement = rootElement;

        return browserDocumentation;
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
                    Number pixelRatio = detectPixelRatio();

                    WebElement optionalRootWebElement = validateAndFindRootElementIfRequired();
                    Path screenshot = createScreenshot(optionalRootWebElement, pixelRatio, screenshotName);
                    createAnnotations(optionalRootWebElement, pixelRatio, screenshotName);

                    return screenshot;
                });

        step.execute(StepReportOptions.REPORT_ALL);
    }

    private WebElement validateAndFindRootElementIfRequired() {
        if (rootElement == null) {
            return null;
        }

        rootElement.should(new VisibleValueMatcher());
        return this.rootElement.findElement();
    }

    private Path createScreenshot(WebElement optionalRootWebElement, Number pixelRatio, String screenshotName) {
        DocumentationArtifacts.registerName(screenshotName);

        Screenshot screenshot = new Screenshot(getScreenshotTaker(),
                pixelRatio,
                rootElementLocationAndSizerProvider(optionalRootWebElement));

        String artifactName = screenshotName + ".png";

        Path screenShotPath = getCfg().getDocArtifactsPath().resolve(artifactName);
        FileUtils.createDirsForFile(screenShotPath);
        screenshot.save(screenShotPath);

        return screenShotPath;
    }

    private WebElementLocationAndSizeProvider rootElementLocationAndSizerProvider(WebElement optionalRootWebElement) {
        if (optionalRootWebElement == null) {
            return null;
        }

        return new WebElementLocationAndSizeProvider() {
            @Override
            public Point getLocation() {
                return optionalRootWebElement.getLocation();
            }

            @Override
            public Dimension getSize() {
                return optionalRootWebElement.getSize();
            }
        };
    }

    private TakesScreenshot getScreenshotTaker() {
        return (TakesScreenshot) this.driver;
    }

    private void createAnnotations(WebElement optionalRootWebElement, Number pixelRatio, String screenshotName) {
        Point rootPoint = findRootLocation(optionalRootWebElement);
        List<? extends Map<String, ?>> shapes = annotations.stream()
                .map((annotation) -> createAnnotationData(annotation, rootPoint)).collect(toList());

        Map<String, Object> result = new HashMap<>();
        result.put("shapes", shapes);
        result.put("pixelRatio", pixelRatio);

        String annotationsJson = JsonUtils.serializePrettyPrint(result);
        FileUtils.writeTextContent(getCfg().getDocArtifactsPath().resolve(Paths.get(screenshotName + ".json")),
                annotationsJson);
    }

    private Map<String, ?> createAnnotationData(ImageAnnotation annotation, Point rootPoint) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", annotation.getId());
        data.put("type", annotation.getType());
        data.put("text", annotation.getText());
        data.put("color", annotation.getColor());
        data.put("darkFriendly", annotation.isDarkFriendly());
        // TODO this is temporary for znai compatibility - remove after znai new version release
        data.put("invertedColors", annotation.isDarkFriendly());

        PageElement pageElement = annotation.getPageElement();

        pageElement.should(new VisibleValueMatcher());
        annotation.addAnnotationData(data, createAdjustedForRootLocationAndSizeProvider(rootPoint,
                pageElement.findElement()));

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

    private Number detectPixelRatio() {
        Object pixelRatio = ((JavascriptExecutor) driver).executeScript("return window.devicePixelRatio");
        return pixelRatio instanceof Number ? (Number) pixelRatio : 1;
    }

    private WebElementLocationAndSizeProvider createAdjustedForRootLocationAndSizeProvider(Point rootPoint,
                                                                                           WebElement webElement) {
        return new WebElementLocationAndSizeProvider() {
            @Override
            public Point getLocation() {
                Point location = webElement.getLocation();
                return new Point(location.getX() - rootPoint.getX(), location.getY() - rootPoint.getY());
            }

            @Override
            public Dimension getSize() {
                return webElement.getSize();
            }
        };
    }

    private Point findRootLocation(WebElement optionalRootWebElement) {
        if (optionalRootWebElement == null) {
            return new Point(0, 0);
        }

        return optionalRootWebElement.getLocation();
    }
}
