/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.cache;

import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.reporter.MessageToken;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.reporter.WebTauStepInputKeyValue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.stringValue;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class Cache {
    public static final Cache cache = new Cache();

    private final FileBasedCache fileBasedCache;

    private Cache() {
        fileBasedCache = new FileBasedCache(() -> WebTauConfig.getCfg().getCachePath());
    }

    public <E> CachedValue<E> value(String id) {
        return new CachedValue<>(cache, id);
    }

    public <E> E get(String key) {
        return getAsStep(key, Function.identity());
    }

    public <E> E get(String key, long expirationMs, Supplier<E> newValueSupplier) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(action("getting cached or generating new value"), FROM, id(key)),
                (r) -> {
                    @SuppressWarnings("unchecked")
                    CachedValueAndMethod<E> cachedValueAndMethod = (CachedValueAndMethod<E>) r;

                    MessageToken preposition = cachedValueAndMethod.method == ObtainMethod.CACHED ? FROM : AS;
                    return tokenizedMessage(action(cachedValueAndMethod.method.message), preposition, id(key), COLON, stringValue(cachedValueAndMethod.value));
                },
                () -> getWithExpirationAndSupplierStep(key, expirationMs, newValueSupplier, Function.identity()));

        step.setInput(WebTauStepInputKeyValue.stepInput(Collections.singletonMap("expirationMs", expirationMs)));

        CachedValueAndMethod<E> executionResult = step.execute(StepReportOptions.REPORT_ALL);
        return executionResult.value;

    }

    public boolean exists(String key) {
        MessageToken valuePresenceMessage = action("cache value presence");
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(action("check"), id(key), valuePresenceMessage),
                (result) -> tokenizedMessage(action("checked"), id(key), valuePresenceMessage, COLON,
                        classifier((boolean)result ? "exists" : "absent")),
                () -> fileBasedCache.exists(key));

        return step.execute(StepReportOptions.SKIP_START);
    }

    public void remove(String key) {
        MessageToken valueMessage = action("cached value");

        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(action("remove"), id(key), valueMessage),
                () -> tokenizedMessage(action("removed"), id(key), valueMessage),
                () -> fileBasedCache.remove(key));

        step.execute(StepReportOptions.SKIP_START);
    }

    public boolean isExpired(String key, long expirationMs) {
        MessageToken valueExpirationMessage = action("cache value expiration");
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(action("check"), id(key), valueExpirationMessage),
                (result) -> tokenizedMessage(action("checked"), id(key), valueExpirationMessage, COLON,
                        classifier((boolean)result ? "expired" : "valid")),
                () -> fileBasedCache.isExpired(key, expirationMs));

        step.setInput(WebTauStepInputKeyValue.stepInput("expirationMs", expirationMs));

        return step.execute(StepReportOptions.SKIP_START);
    }

    public Path getAsPath(String key) {
        return getAsStep(key, (v) -> Paths.get(v.toString()));
    }

    public void put(String key, Object value) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(action("caching value"), AS, id(key), COLON, stringValue(value)),
                () -> tokenizedMessage(action("cached value"), AS, id(key), COLON, stringValue(value)),
                () -> fileBasedCache.put(key, CacheValueConverter.convertToCached(value)));

        step.execute(StepReportOptions.SKIP_START);
    }

    private <E, R> R getAsStep(String key, Function<E, R> converter) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(action("getting cached value"), FROM, id(key)),
                (r) -> tokenizedMessage(action("got cached value"), FROM, id(key), COLON, stringValue(r)),
                () -> {
                    E value = fileBasedCache.get(key);
                    if (value == null) {
                        throw new RuntimeException("can't find cached value by key: " + key);
                    }

                    return converter.apply(value);
                });

        return step.execute(StepReportOptions.SKIP_START);
    }

    private <E, R> CachedValueAndMethod<R> getWithExpirationAndSupplierStep(String key, long expirationMs, Supplier<E> newValueSupplier, Function<E, R> converter) {
        if (!exists(key)) {
            E newValue = newValueSupplier.get();
            put(key, newValue);

            return new CachedValueAndMethod<>(ObtainMethod.CREATE_NEW, converter.apply(newValue));
        } else if (isExpired(key, expirationMs)) {
            E newValue = newValueSupplier.get();
            put(key, newValue);

            return new CachedValueAndMethod<>(ObtainMethod.RE_CREATE, converter.apply(newValue));
        } else {
            E existingValue = get(key);
            return new CachedValueAndMethod<>(ObtainMethod.CACHED, converter.apply(existingValue));
        }
    }

    enum ObtainMethod {
        CREATE_NEW("created new value and cached"),
        RE_CREATE("re-created value and cached"),
        CACHED("got cached value");

        private final String message;

        ObtainMethod(String message) {
            this.message = message;
        }
    }

    static class CachedValueAndMethod<R> {
        private final ObtainMethod method;
        private final R value;

        public CachedValueAndMethod(ObtainMethod method, R value) {
            this.method = method;
            this.value = value;
        }
    }
}
