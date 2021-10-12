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
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

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
                        throw new AssertionError("can't find cached value by key: " + key);
                    }

                    return converter.apply(value);
                });

        return step.execute(StepReportOptions.SKIP_START);
    }
}
