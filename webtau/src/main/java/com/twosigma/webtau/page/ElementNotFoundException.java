package com.twosigma.webtau.page;

public class ElementNotFoundException extends AssertionError {
    public ElementNotFoundException(String message) {
        super(message);
    }
}
