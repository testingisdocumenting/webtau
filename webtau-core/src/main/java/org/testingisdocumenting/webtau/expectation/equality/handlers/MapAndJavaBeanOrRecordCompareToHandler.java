/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.testingisdocumenting.webtau.data.converters.ObjectProperties;
import org.testingisdocumenting.webtau.utils.JavaBeanUtils;
import org.testingisdocumenting.webtau.utils.JavaRecordUtils;

import java.util.Map;

public class MapAndJavaBeanOrRecordCompareToHandler extends MapAsExpectedCompareToHandlerBase {
    @Override
    protected boolean handleEquality(Object actual) {
        return !(actual instanceof Iterable || actual instanceof Map);
    }

    @Override
    public Object convertedActual(Object actual, Object expected) {
        if (actual instanceof ObjectProperties) {
            return ((ObjectProperties) actual).getTopLevelProperties();
        }

        if (JavaRecordUtils.isRecord(actual)) {
            return JavaRecordUtils.convertRecordToMap(actual);
        }

        return JavaBeanUtils.convertBeanToMap(actual);
    }
}
