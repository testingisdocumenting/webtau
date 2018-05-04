package com.twosigma.webtau.page.path;

import com.twosigma.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface ElementsFinder {
    List<WebElement> find(WebDriver driver);

    /**
     * @param isFirst isFirst is this the first entry in the path
     * @return tokenized message
     */
    TokenizedMessage description(boolean isFirst);
}
