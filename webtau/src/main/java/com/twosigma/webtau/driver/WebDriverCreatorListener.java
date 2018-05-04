package com.twosigma.webtau.driver;

import org.openqa.selenium.WebDriver;

public interface WebDriverCreatorListener {
    void beforeDriverCreation();
    void afterDriverCreation(WebDriver webDriver);
}
