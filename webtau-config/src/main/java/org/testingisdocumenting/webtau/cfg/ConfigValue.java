/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.cfg;

import org.testingisdocumenting.webtau.persona.Persona;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ConfigValue {
    static final String ENV_VAR_PREFIX = "WEBTAU_";
    private static final String CAMEL_CASE_PATTERN = "([a-z])([A-Z]+)";

    private final String key;
    private final String prefixedUpperCaseKey;
    private final Supplier<Object> defaultValueSupplier;
    private final String description;

    private final Map<String, Deque<Value>> valuesPerPersonaId;

    private final boolean isBoolean;

    public static ConfigValue declare(String key, String description, Supplier<Object> defaultValueSupplier) {
        return new ConfigValue(key, description, false, defaultValueSupplier);
    }

    public static ConfigValue declareBoolean(String key, String description, Boolean defaultValue) {
        return new ConfigValue(key, description, true, () -> defaultValue);
    }

    private ConfigValue(String key, String description, boolean isBoolean, Supplier<Object> defaultValueSupplier) {
        this.key = key;
        this.prefixedUpperCaseKey = ENV_VAR_PREFIX + convertToSnakeCase(key);
        this.description = description;
        this.isBoolean = isBoolean;
        this.defaultValueSupplier = defaultValueSupplier;

        this.valuesPerPersonaId = new HashMap<>();
        this.valuesPerPersonaId.put(Persona.DEFAULT_PERSONA_ID, new ArrayDeque<>());

        reset();
    }

    public void set(String source, Object value) {
        set(source, Persona.getCurrentPersona().getId(), value);
    }

    public void set(String source, String personaId, Object value) {
        Deque<Value> values = getOrCreatePersonaValues(personaId);
        values.addFirst(new Value(source, value));
    }

    public void reset() {
        valuesPerPersonaId.clear();
        valuesPerPersonaId.put(Persona.DEFAULT_PERSONA_ID, new ArrayDeque<>());
    }

    public void accept(String source, Map<String, ?> configValues) {
        accept(source, Persona.DEFAULT_PERSONA_ID, configValues);
    }

    public void accept(String source, String personaId, Map<String, ?> configValues) {
        if (configValues.containsKey(key)) {
            set(source, personaId, configValues.get(key));
        } else if (configValues.containsKey(prefixedUpperCaseKey)) {
            set(source, personaId, configValues.get(prefixedUpperCaseKey));
        }
    }

    public boolean match(String configKey) {
        return configKey.equals(key) || configKey.equals(prefixedUpperCaseKey);
    }

    public String getKey() {
        return key;
    }

    public String getPrefixedUpperCaseKey() {
        return prefixedUpperCaseKey;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return (isDefault() ? "default" : currentOrDefaultPersonaValuesForRead().getFirst().getSourceId());
    }

    public List<String> getSources() {
        return (isDefault() ?
                Collections.singletonList("default") :
                currentOrDefaultPersonaValuesForRead().stream().map(Value::getSourceId).collect(Collectors.toList()));
    }

    public boolean isBoolean() {
        return isBoolean;
    }

    public Object getAsObject() {
        return isDefault() ? defaultValueSupplier.get():
                currentOrDefaultPersonaValuesForRead().getFirst().getValue();
    }

    public String getAsString() {
        return convertToString(getAsObject());
    }

    public Path getAsPath() {
        return isDefault() ? (Path) defaultValueSupplier.get() : Paths.get(getAsObject().toString());
    }

    public int getAsInt() {
        if (isDefault()) {
           return (int) defaultValueSupplier.get();
        }

        Object first = getAsObject();
        return first instanceof Integer ?
                (int) first :
                Integer.parseInt(first.toString());
    }

    public long getAsLong() {
        if (isDefault()) {
           return (long) defaultValueSupplier.get();
        }

        Object first = getAsObject();
        return first instanceof Long ?
                (long) first :
                Long.parseLong(first.toString());
    }

    public boolean getAsBoolean() {
        if (isDefault()) {
            return (boolean) defaultValueSupplier.get();
        }

        Object first = getAsObject();
        return first.toString().equalsIgnoreCase("true");
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAsList() {
        return (List<T>) getAsObject();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getAsMap() {
        return (Map<String, Object>) getAsObject();
    }

    @Override
    public String toString() {
        return valuesPerPersonaId.keySet().stream().map(this::renderPersonaValues).collect(Collectors.joining("\n"));
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("key", key);
        result.put("value", getAsObject());
        result.put("source", getSource());

        return result;
    }

    public boolean isDefault() {
        return currentOrDefaultPersonaValuesForRead().isEmpty();
    }

    public boolean nonDefault() {
        return ! isDefault();
    }

    public Object getDefaultValue() {
        return defaultValueSupplier.get();
    }

    private String renderPersonaValues(String personaId) {
        String title = personaId.isEmpty() ? "" : "persona " + personaId + ":\n";
        return title + key + ": " + personaIdOrDefaultPersonaValuesForRead(personaId).stream()
                .map(Value::toString)
                .collect(Collectors.joining(", "));
    }

    private String convertToString(Object value) {
        return value == null ? "" : value.toString();
    }

    private String convertToSnakeCase(String key) {
        return key.replaceAll(CAMEL_CASE_PATTERN, "$1_$2")
                .toUpperCase();
    }

    private Deque<Value> currentOrDefaultPersonaValuesForRead() {
        return personaIdOrDefaultPersonaValuesForRead(Persona.getCurrentPersona().getId());
    }

    private Deque<Value> personaIdOrDefaultPersonaValuesForRead(String personaId) {
        Deque<Value> values = valuesPerPersonaId.get(personaId);
        return values != null ? values : valuesPerPersonaId.get(Persona.DEFAULT_PERSONA_ID);
    }

    private Deque<Value> getOrCreatePersonaValues(String personaId) {
        return valuesPerPersonaId.computeIfAbsent(personaId, (k) -> new ArrayDeque<>());
    }

    private static class Value {
        private final String sourceId;
        private final Object value;

        public Value(String sourceId, Object value) {
            this.sourceId = sourceId;
            this.value = value;
        }

        public String getSourceId() {
            return sourceId;
        }

        public Object getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value + " (" + sourceId + ")";
        }
    }
}
