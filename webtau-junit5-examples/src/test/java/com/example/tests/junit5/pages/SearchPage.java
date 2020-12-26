package com.example.tests.junit5.pages;

import org.openqa.selenium.Keys;
import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.browser.page.ElementValue;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

public class SearchPage {
    private final PageElement box = $("#search-box");
    private final PageElement results = $("#results .result");
    public ElementValue<Integer> numberOfResults = results.getCount();

    public void submit(String query) {
        browser.open("/search");

        box.setValue(query);
        box.sendKeys(Keys.ENTER.toString());
    }
}
