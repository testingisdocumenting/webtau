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
    private final ConfigValueHolder root

    private Value value
    public final Map<String, Value> valuePerPersona

    static ConfigValueHolder withPersona(String personaId, String name, Object value) {
        return new ConfigValueHolder(personaId, name, EMPTY, value)
    }

    static ConfigValueHolder withRoot(String name, ConfigValueHolder root) {
        return new ConfigValueHolder(DEFAULT_PERSONA_ID, name, root, null)
    }

    static ConfigValueHolder withRootAndPersona(String personaId, String name, ConfigValueHolder commonValueHolder) {
        return new ConfigValueHolder(personaId, name, commonValueHolder, null)
    }

    static ConfigValueHolder withNameOnly(String name) {
        return new ConfigValueHolder(DEFAULT_PERSONA_ID, name, EMPTY, null)
    }

    private ConfigValueHolder(String personaId, String name, ConfigValueHolder root, Object value) {
        this.@holderName = name
        this.@root = root
        this.@personaId = personaId
        this.@value = new Value(value)
        this.@valuePerPersona = new HashMap<>()
    }

    def makeCopy(String personaId) {
        def copy = new ConfigValueHolder(personaId, this.@holderName, this.@root, null)
        copy.@value = this.@value.copy()

        return copy
    }

    Object getProperty(String name) {
        def holder = this.@value.map.get(name)
        if (holder != null) {
            return holder
        }

        def fromRoot = findInRootAndMakeCopy(name)
        if (fromRoot != null) {
            this.@value.map.put(name, fromRoot)
            return fromRoot
        }

        def newHolder = new ConfigValueHolder(this.@personaId, concatPath(this.@holderName, name), EMPTY, null)
        this.@value.map.put(name, newHolder)

        if (this.@personaId != DEFAULT_PERSONA_ID) {
            this.@root.@valuePerPersona.put(this.@personaId, newHolder)
        }

        return newHolder
    }

    void setProperty(String name, Object value) {
        println("${this.@personaId}: $name = $value")
        if (this.@value.value != null) {
            throw new IllegalArgumentException("config value <$name> is set to be not a map: ${this.@value.value}")
        }

        def newValue = withPersona(this.@personaId, this.@holderName + "." + name, value)
        this.@value.map.put(name, newValue)

        if (this.@personaId != DEFAULT_PERSONA_ID) {
            def personaValues = this.@valuePerPersona.get(this.@personaId)
            if (personaValues == null) {
                personaValues = this.@root.makeCopy(this.@personaId).@value
                personaValues.@map.put(name, newValue)
                this.@root.@valuePerPersona.put(this.@personaId, personaValues)
            } else {
                personaValues.@map.put(name, newValue)
            }
        }
    }

    Map<String, Object> toMap() {
        return this.@value.convertToMap()
    }

//
//    private ConfigValueHolder findInRootAndMakeCopy(String name) {
//        // in case of environment override we need the copy of root values that represent map like object
//        // so environment/persona specific override can override a single value within a map or add a new value
//        //
//        def fromRoot = (this.@root.@value.map != null && !this.@root.@value.map.isEmpty()) ?
//                this.@root.getProperty(name) : null
//        if (fromRoot instanceof ConfigValueHolder) {
//            def copy = fromRoot.makeCopy(this.@personaId)
//            this.@root.@valuePerPersona.put(this.@personaId, copy.@value)
//
//            return copy
//        }
//
//        return null
//    }

    private ConfigValueHolder findInRootAndMakeCopy(String name) {
        def fromRoot = findInRootAndMakeCopy(this.@root, name)
        if (fromRoot == null) {
            fromRoot = findInRootAndMakeCopy(this.@root.@root)
        }

        if (fromRoot != null) {
            this.@root.@valuePerPersona.put(this.@personaId, fromRoot.@value)
        }

        return fromRoot
    }

    private ConfigValueHolder findInRootAndMakeCopy(ConfigValueHolder root, String name) {
        // in case of environment override we need the copy of root values that represent map like object
        // so environment/persona specific override can override a single value within a map or add a new value
        //
        def fromRoot = (root.@value.map != null && !root.@value.map.isEmpty()) ?
                root.getProperty(name) : null
        if (fromRoot instanceof ConfigValueHolder) {
            def copy = fromRoot.makeCopy(this.@personaId)
//            root.@valuePerPersona.put(this.@personaId, copy.@value)

            return copy
        }

        return null
    }

    private static String concatPath(String left, String right) {
        return left + "." + right
    }
}
