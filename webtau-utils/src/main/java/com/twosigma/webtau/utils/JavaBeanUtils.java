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

package com.twosigma.webtau.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class JavaBeanUtils {
    private JavaBeanUtils() {
    }

    public static Map<String, ?> convertBeanToMap(Object bean) {
        if (bean == null) {
            return Collections.emptyMap();
        }

        try {
            return extractMap(bean);
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, ?> extractMap(Object bean) throws IntrospectionException,
            InvocationTargetException,
            IllegalAccessException {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : properties) {
            if (!property.getName().equals("class")) {
                result.put(property.getName(), property.getReadMethod().invoke(bean));
            }
        }

        return result;
    }
}
