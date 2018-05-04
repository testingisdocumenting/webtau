package com.twosigma.webtau.page.path;

import com.twosigma.webtau.reporter.TokenizedMessage;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface ElementsFilter {
    List<WebElement> filter(List<WebElement> original);
    TokenizedMessage description();
}
