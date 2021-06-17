package com.example.tests.junit5;

import org.junit.jupiter.api.Test;
import org.testingisdocumenting.webtau.junit5.WebTau;

import static com.example.tests.junit5.pages.Pages.*;
import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
public class WebSearchJavaTest {
    @Test
    public void searchByQuery() {
        search.submit("search this");
        search.numberOfResults.waitToBe(greaterThan(1));
    }
}
