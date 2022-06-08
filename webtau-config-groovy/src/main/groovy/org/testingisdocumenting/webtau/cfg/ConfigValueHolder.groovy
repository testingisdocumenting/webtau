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
                if (v.@value.value != null) {
                    return v.@value.value
                }

                return v.toMap()
            } else {
                return v
            }
        }

        @Override
        String toString() {
            if (value != null) {
                return value.toString()
            }

            return map.toString()
        }
    }

    private String holderName

    // environment and persona specific overrides. will take a copy of values from there if found
    private final List<ConfigValueHolder> roots

    private Value value

    static ConfigValueHolder withValue(String name, Object value) {
        return new ConfigValueHolder(name, Collections.emptyList(), value)
    }

    static ConfigValueHolder withRoots(String name, List<ConfigValueHolder> roots) {
        return new ConfigValueHolder(name, roots, null)
    }

    static ConfigValueHolder withNameAndRoots(String name, List<ConfigValueHolder> roots) {
        return new ConfigValueHolder(name, roots, null)
    }

    static ConfigValueHolder withNameOnly(String name) {
        return new ConfigValueHolder(name, Collections.emptyList(), null)
    }

    private ConfigValueHolder(String name, List<ConfigValueHolder> roots, Object value) {
        this.@holderName = name
        this.@roots = roots
        this.@value = new Value(value)
    }

    def makeCopy() {
        def copy = new ConfigValueHolder(this.@holderName + "[copy]",
                new ArrayList<ConfigValueHolder>(this.@roots), null)
        copy.@value = this.@value.copy()

        return copy
    }

    Object getProperty(String name) {
        def holder = this.@value.map.get(name)
        if (holder != null) {
            return simplePropertyOrHolder(holder)
        }

        def fromRoot = findInRootAndMakeCopy(name)
        if (fromRoot != null) {
            this.@value.map.put(name, fromRoot)
            return simplePropertyOrHolder(fromRoot)
        }

        def newHolder = new ConfigValueHolder(concatPath(this.@holderName, name),
                Collections.emptyList(), null)
        this.@value.map.put(name, newHolder)

        return simplePropertyOrHolder(newHolder)
    }

    void setProperty(String name, Object value) {
        if (this.@value.value != null) {
            throw new IllegalArgumentException("config value <$name> is set to be not a map: ${this.@value.value}")
        }

        def newValue = withValue(this.@holderName + "." + name, value)
        this.@value.map.put(name, newValue)
    }

    Map<String, Object> toMap() {
        return this.@value.convertToMap()
    }

    @Override
    String toString() {
        return this.@value.toString()
    }

    private ConfigValueHolder findInRootAndMakeCopy(String name) {
        for (ConfigValueHolder r : this.@roots) {
            def found = findInRootAndMakeCopy(r, name)
            if (found) {
                return found
            }
        }

        return null
    }

    private static ConfigValueHolder findInRootAndMakeCopy(ConfigValueHolder rootToSearch, String name) {
        // in case of environment override we need the copy of root values that represent map like object
        // so environment/persona specific override can override a single value within a map or add a new value
        //
        def fromRoot = (rootToSearch.@value.map != null && !rootToSearch.@value.map.isEmpty()) ?
                rootToSearch.getProperty(name) : null
        if (fromRoot instanceof ConfigValueHolder) {
            return fromRoot.makeCopy()
        }

        return null
    }

    private static Object simplePropertyOrHolder(ConfigValueHolder valueHolder) {
        if (valueHolder.@value.value != null) {
            return valueHolder.@value.value
        }

        return valueHolder
    }

    private static String concatPath(String left, String right) {
        return left + "." + right
    }
}
