/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.cfg

/**
 * holds a single value or a complex value (e.g. map)
 */
class ConfigValueHolder {
    private String holderName
    private Object value
    private Map<String, Object> map

    ConfigValueHolder(String name, Object value) {
        this.@holderName = name

        if (value instanceof Map) {
            this.@map = new LinkedHashMap<>(value)
        } else {
            this.@value = value
        }
    }

    ConfigValueHolder(String name) {
        this.@holderName = name
        this.@map = new LinkedHashMap<>()
    }

    Object getProperty(String name) {
        def holder = this.@map.get(name)
        if (holder == null) {
            holder = new ConfigValueHolder(concatPath(this.@holderName, name))
            this.@map.put(name, holder)
        }

        return holder
    }

    void setProperty(String name, Object value) {
        if (this.@value != null) {
            throw new IllegalArgumentException("config value <$name> is set to be not a map: ${this.@value}")
        }

        this.@map.put(name, new ConfigValueHolder(this.@holderName + "." + name, value))
    }

    Map<String, Object> toMap() {
        return convertMap(this.@map)
    }

    private static Map<String, Object> convertMap(Map<String, Object> toConvert) {
        Map<String, Object> result = new LinkedHashMap<>()
        toConvert.forEach((k, v) -> {
            result.put(k, convertValue(v))
        })

        return result;
    }

    private static Object convertValue(ConfigValueHolder v) {
        if (v.@value) {
            return v.@value
        }

        return v.toMap()
    }

    private static String concatPath(String left, String right) {
        return left + "." + right
    }
}
