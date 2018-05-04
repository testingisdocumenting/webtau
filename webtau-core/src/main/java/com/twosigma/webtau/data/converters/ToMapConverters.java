/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.data.converters;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.twosigma.webtau.utils.ServiceUtils;
import com.twosigma.webtau.utils.TraceUtils;

public class ToMapConverters {
    private static List<ToMapConverter> converters = discover();

    public static Map<String, ?> convert(Object v) {
        if (v == null) {
            return null;
        }

        return converters.stream().
                map(h -> h.convert(v)).
                filter(Objects::nonNull).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException("can't find a map converter for: " + TraceUtils.renderValueAndType(v)));
    }

    private static List<ToMapConverter> discover() {
        List<ToMapConverter> discovered = ServiceUtils.discover(ToMapConverter.class);
        discovered.add(new MapToMapConverter());
        discovered.add(new BeanToMapConverter());

        return discovered;
    }
}
