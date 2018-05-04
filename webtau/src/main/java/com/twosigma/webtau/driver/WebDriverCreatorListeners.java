package com.twosigma.webtau.driver;

import com.twosigma.webtau.utils.ServiceLoaderUtils;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public class WebDriverCreatorListeners {
    private static final Set<WebDriverCreatorListener> listeners = ServiceLoaderUtils.load(WebDriverCreatorListener.class);

    private WebDriverCreatorListeners() {
    }

    public static void add(WebDriverCreatorListener listener) {
        listeners.add(listener);
    }

    public static void remove(WebDriverCreatorListener listener) {
        listeners.remove(listener);
    }

    public static void beforeDriverCreation() {
        listeners.forEach(WebDriverCreatorListener::beforeDriverCreation);
    }

    public static void afterDriverCreation(WebDriver webDriver) {
        listeners.forEach(l -> l.afterDriverCreation(webDriver));
    }
}
