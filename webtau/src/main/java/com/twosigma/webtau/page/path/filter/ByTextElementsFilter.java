package com.twosigma.webtau.page.path.filter;

import com.twosigma.webtau.reporter.TokenizedMessage;
import com.twosigma.webtau.page.path.ElementsFilter;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.selectorType;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.selectorValue;
import static java.util.stream.Collectors.toList;

public class ByTextElementsFilter implements ElementsFilter {
    private String text;

    public ByTextElementsFilter(String text) {
        this.text = text;
    }

    @Override
    public List<WebElement> filter(List<WebElement> original) {
        return original.stream().filter(el -> el.getText().equals(text)).collect(toList());
    }

    @Override
    public TokenizedMessage description() {
        return tokenizedMessage(selectorType("element(s) with text"), selectorValue(text));
    }
}
