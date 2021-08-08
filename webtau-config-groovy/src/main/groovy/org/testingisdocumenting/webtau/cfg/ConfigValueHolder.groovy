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
    // need to use this class and a map per persona below
    static class Value {
        private Object value
        private Map<String, Object> map

        Value(Object v) {
            if (v instanceof Map) {
                this.@map = new LinkedHashMap<>(v)
            } else {
                this.@value = v
                this.@map = new LinkedHashMap<>()
            }
        }

        Value copy() {
            def result = new Value(this.value)
            result.map = new LinkedHashMap<>(this.map)

            return result
        }

        Map<String, Object> convertToMap() {
            return convertMap(this.map)
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
                if (v.@value.value) {
                    return v.@value.value
                }

                return v.toMap()
            } else {
                return v
            }
        }


    }

    private static final String DEFAULT_PERSONA_ID = ""
    static final ConfigValueHolder EMPTY = withNameOnly("__empty")

    private String holderName

    private final String personaId

    // environment and persona specific override. will take a copy of values from here
    private final ConfigValueHolder commonValueHolder

    private Value value
    public final Map<String, Value> valuePerPersona

    static ConfigValueHolder fromValueWithPersona(String personaId, String name, Object value) {
        return new ConfigValueHolder(personaId, name, EMPTY, value)
    }

    static ConfigValueHolder fromValueWithDefaultPersona(String name, Object value) {
        return fromValueWithPersona(DEFAULT_PERSONA_ID, name, value)
    }

    static ConfigValueHolder withCommonValueHolder(String name, ConfigValueHolder commonValueHolder) {
        return new ConfigValueHolder(DEFAULT_PERSONA_ID, name, commonValueHolder, null)
    }

    static ConfigValueHolder withCommonValueHolderAndPersona(String personaId, String name, ConfigValueHolder commonValueHolder) {
        return new ConfigValueHolder(personaId, name, commonValueHolder, null)
    }

    static ConfigValueHolder withNameOnly(String name) {
        return new ConfigValueHolder(DEFAULT_PERSONA_ID, name, EMPTY, null)
    }

    private ConfigValueHolder(String personaId, String name, ConfigValueHolder commonValueHolder, Object value) {
        this.@holderName = name
        this.@commonValueHolder = commonValueHolder
        this.@personaId = personaId
        this.@value = new Value(value)
        this.@valuePerPersona = new HashMap<>()
    }

    def makeCopy(String personaId) {
        def copy = new ConfigValueHolder(personaId, this.@holderName, this.@commonValueHolder, null)
        copy.@value = this.@value.copy()

        return copy
    }

    Object getProperty(String name) {
        def holder = this.@value.map.get(name)
        if (holder != null) {
            return holder
        }

        def fromCommon = findInCommon(name)
        if (fromCommon != null) {
            this.@value.map.put(name, fromCommon)
            return fromCommon
        }

        def newHolder = new ConfigValueHolder(this.@personaId, concatPath(this.@holderName, name), EMPTY, null)
        this.@value.map.put(name, newHolder)

        return newHolder
    }

    void setProperty(String name, Object value) {
        println("${this.@personaId}: $name = $value")
        if (this.@value.value != null) {
            throw new IllegalArgumentException("config value <$name> is set to be not a map: ${this.@value.value}")
        }

        def newValue = fromValueWithPersona(this.@personaId, this.@holderName + "." + name, value)
        this.@value.map.put(name, newValue)

        if (this.@personaId != DEFAULT_PERSONA_ID) {
            def personaValues = this.@valuePerPersona.get(this.@personaId)
            if (personaValues == null) {
                personaValues = this.@commonValueHolder.makeCopy(this.@personaId).@value
                personaValues.@map.put(name, newValue)
                this.@commonValueHolder.@valuePerPersona.put(this.@personaId, personaValues)
            } else {
                personaValues.@map.put(name, newValue)
            }
        }
    }

    Map<String, Object> toMap() {
        return this.@value.convertToMap()
    }

    private ConfigValueHolder findInCommon(String name) {
        // in case of environment override we need the copy of common values that represent map like object
        // so environment specific override can override a single value within a map or add a new value
        //
        def fromCommon = (this.@commonValueHolder.@value.map != null && !this.@commonValueHolder.@value.map.isEmpty()) ?
                this.@commonValueHolder.getProperty(name) : null
        if (fromCommon instanceof ConfigValueHolder) {
            def copy = fromCommon.makeCopy(this.@personaId)
            this.@commonValueHolder.@valuePerPersona.put(this.@personaId, copy)

            return copy
        }

        return null
    }

    private static String concatPath(String left, String right) {
        return left + "." + right
    }
}
