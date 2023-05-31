/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.LinkedHashMap;
import java.util.Map;

public class JavaRecordUtils {
    private JavaRecordUtils() {
    }

    public static boolean isRecord(Object v) {
        if (v == null) {
            return false;
        }

        return v.getClass().isRecord();
    }

    public static Map<String, ?> convertRecordToMap(Object v) {
        if (!v.getClass().isRecord()) {
            return null;
        }

        RecordComponent[] recordComponents = v.getClass().getRecordComponents();
        Map<String, Object> result = new LinkedHashMap<>();
        for (RecordComponent component : recordComponents) {
            try {
                result.put(component.getName(), component.getAccessor().invoke(v));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
}
