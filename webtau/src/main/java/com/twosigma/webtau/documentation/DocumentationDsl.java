package com.twosigma.webtau.documentation;

import com.twosigma.webtau.cfg.WebTauConfig;
import com.twosigma.webtau.documentation.annotations.ArrowImageAnnotation;
import com.twosigma.webtau.documentation.annotations.BadgeImageAnnotation;
import com.twosigma.webtau.documentation.annotations.HighlighterImageAnnotation;
import com.twosigma.webtau.documentation.annotations.ImageAnnotation;
import com.twosigma.webtau.page.PageElement;
import com.twosigma.webtau.utils.FileUtils;
import com.twosigma.webtau.utils.JsonUtils;
import org.openqa.selenium.*;

import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class DocumentationDsl {
    private WebTauConfig cfg = WebTauConfig.INSTANCE;

    private TakesScreenshot screenshotTaker;
    private List<ImageAnnotation> annotations;
    private WebDriver driver;

    public DocumentationDsl(WebDriver driver) {
        this.driver = driver;
        this.screenshotTaker = (TakesScreenshot) driver;
    }

    private DocumentationDsl(WebDriver driver, List<ImageAnnotation> annotations) {
        this(driver);
        this.annotations = annotations;
    }

    public DocumentationDsl withAnnotations(ImageAnnotation... annotations) {
        return new DocumentationDsl(driver, assignDefaultText(Arrays.asList(annotations)));
    }

    public static ImageAnnotation badge(PageElement pageElement) {
        return new BadgeImageAnnotation(pageElement, "");
    }

    public static ImageAnnotation highlighter(PageElement pageElement) {
        return new HighlighterImageAnnotation(pageElement);
    }

    public static ImageAnnotation arrow(PageElement pageElement, String text) {
        return new ArrowImageAnnotation(pageElement, text);
    }

    public void capture(String screenshotName) {
        createScreenshot(screenshotName);
        createAnnotations(screenshotName);
    }

    private void createScreenshot(String screenshotName) {
        Screenshot screenshot = new Screenshot(screenshotTaker);
        screenshot.save(cfg.getDocArtifactsPath().resolve(screenshotName + ".png"));
    }

    private void createAnnotations(String screenshotName) {
        List<? extends Map<String, ?>> shapes = annotations.stream().map(this::createAnnotationData).collect(toList());

        Map<String, Object> result = new HashMap<>();
        result.put("shapes", shapes);
        result.put("pixelRatio", getPixelRatio());

        String annotationsJson = JsonUtils.serializePrettyPrint(result);
        FileUtils.writeTextContent(cfg.getDocArtifactsPath().resolve(Paths.get(screenshotName + ".json")),
                annotationsJson);
    }

    private Map<String, ?> createAnnotationData(ImageAnnotation annotation) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", annotation.getId());
        data.put("type", annotation.getType());
        data.put("text", annotation.getText());
        data.put("color", annotation.getColor());

        annotation.addAnnotationData(data, annotation.getPageElement().findElement());

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
