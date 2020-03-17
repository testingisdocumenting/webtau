/*
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

package org.testingisdocumenting.webtau.browser.page;

import org.testingisdocumenting.webtau.browser.page.value.ElementValue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;

public class PageUrl {
    private final Supplier<String> currentUrlSupplier;

    public PageUrl(Supplier<String> currentUrlSupplier) {
        this.currentUrlSupplier = currentUrlSupplier;
    }

    public final ElementValue<String, BrowserContext> full =
            new ElementValue<>(new BrowserContext(), "full page url", this::fetchUrl);

    public final ElementValue<String, BrowserContext> path =
            new ElementValue<>(new BrowserContext(), "page url path", this::fetchPath);

    public final ElementValue<String, BrowserContext> query =
            new ElementValue<>(new BrowserContext(), "page url query", this::fetchQuery);

    public final ElementValue<String, BrowserContext> ref =
            new ElementValue<>(new BrowserContext(), "page url ref", this::fetchRef);

    public String get() {
        return fetchUrl();
    }

    private String fetchUrl() {
        return currentUrlSupplier.get();
    }

    private String fetchPath() {
        return fetchAsUrl().getPath();
    }

    private String fetchQuery() {
        return fetchAsUrl().getQuery();
    }

    private String fetchRef() {
        return fetchAsUrl().getRef();
    }

    private URL fetchAsUrl() {
        try {
            return new URL(fetchUrl());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
