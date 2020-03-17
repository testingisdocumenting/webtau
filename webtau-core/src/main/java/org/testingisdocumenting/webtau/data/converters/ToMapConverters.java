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

package org.testingisdocumenting.webtau.data.converters;

import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.List;
import java.util.Map;

public class ToMapConverters {
    private static List<ToMapConverter> converters = discover();

    public static Map<String, ?> convert(Object v) {
        return TypeConvertersUtils.convertAndThrow(converters.stream(), "map", v);
    }

    private static List<ToMapConverter> discover() {
        List<ToMapConverter> discovered = ServiceLoaderUtils.load(ToMapConverter.class);
        discovered.add(new MapToMapConverter());
        discovered.add(new BeanToMapConverter());

        return discovered;
    }
}
