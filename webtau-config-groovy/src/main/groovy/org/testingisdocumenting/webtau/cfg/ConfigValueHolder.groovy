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
    static final ConfigValueHolder EMPTY = new ConfigValueHolder("__empty")

    private String holderName
    private Object value
    private Map<String, Object> map

    // environment specific override will take a copy of values from here
    private final ConfigValueHolder commonValueHolder

    ConfigValueHolder(String name, Object value) {
        this.@commonValueHolder = EMPTY
        this.@holderName = name

        if (value instanceof Map) {
            this.@map = new LinkedHashMap<>(value)
        } else {
            this.@value = value
        }
    }

    ConfigValueHolder(String name, ConfigValueHolder commonValueHolder) {
        this.@holderName = name
        this.@map = new LinkedHashMap<>()
        this.@commonValueHolder = commonValueHolder
    }

    ConfigValueHolder(String name) {
        this(name, EMPTY)
    }

    def makeCopy() {
        def copy = new ConfigValueHolder(this.@holderName, this.@commonValueHolder)
        copy.@map = new LinkedHashMap<>(this.@map)

        return copy
    }

    Object getProperty(String name) {
        def holder = this.@map.get(name)
        if (holder != null) {
            return holder
        }

        def fromCommon = findInCommon(name)
        if (fromCommon != null) {
            this.@map.put(name, fromCommon)
            return fromCommon
        }

        def newHolder = new ConfigValueHolder(concatPath(this.@holderName, name))
        this.@map.put(name, newHolder)

        return newHolder
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

    private ConfigValueHolder findInCommon(String name) {
        // in case of environment override we need the copy of common values that represent map like object
        // so environment specific override can override a single value within a map or add a new value
        //
        def fromCommon = (this.@commonValueHolder.@map != null && !this.@commonValueHolder.@map.isEmpty()) ?
                this.@commonValueHolder.getProperty(name) : null
        if (fromCommon instanceof ConfigValueHolder) {
            return fromCommon.makeCopy()
        }

        return null
    }

    private static Map<String, Object> convertMap(Map<String, Object> toConvert) {
        Map<String, Object> result = new LinkedHashMap<>()
        toConvert.forEach((k, v) -> {
            result.put(k, convertValue(v))
        })

        return result
    }

    private static Object convertValue(Object v) {
        if (v instanceof ConfigValueHolder) {
            if (v.@value) {
                return v.@value
            }

            return v.toMap()
        } else {
            return v
        }
    }

    private static String concatPath(String left, String right) {
        return left + "." + right
    }
}
