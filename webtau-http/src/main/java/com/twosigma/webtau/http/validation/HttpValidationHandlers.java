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

package com.twosigma.webtau.http.validation;

import com.twosigma.webtau.utils.ServiceLoaderUtils;

import java.util.List;

public class HttpValidationHandlers {
    private static final List<HttpValidationHandler> configurations = ServiceLoaderUtils.load(HttpValidationHandler.class);

    public static void register(HttpValidationHandler handler) {
        configurations.add(handler);
    }

    public static void reset() {
        configurations.clear();
        configurations.addAll(ServiceLoaderUtils.load(HttpValidationHandler.class));
    }

    public static void validate(HttpValidationResult validationResult) {
        configurations.forEach(c -> c.validate(validationResult));
    }
}
