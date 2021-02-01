/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.db;

import java.util.*;

class DbNamedParamsQuery {
    private final List<Object> values;
    private final Object[] valuesArray;
    private final String namedParamsQuery;
    private final Map<String, Object> params;
    private final Set<String> uniqueParamNames;

    private final StringBuilder currentNamedParam;
    private final StringBuilder questionMarksQuery;

    private final boolean hasSingleNoNameParameter;

    private boolean insideParamName;
    private boolean insideSingleQuote;
    private boolean insideDoubleQuote;

    public DbNamedParamsQuery(String namedParamsQuery, Object singleParam) {
        this(namedParamsQuery, singleNoNameParam(singleParam));
    }

    public DbNamedParamsQuery(String namedParamsQuery, Map<String, Object> params) {
        this.namedParamsQuery = namedParamsQuery;
        this.uniqueParamNames = new TreeSet<>();
        this.values = new ArrayList<>();
        this.currentNamedParam = new StringBuilder();
        this.questionMarksQuery = new StringBuilder();
        this.insideParamName = false;
        this.insideSingleQuote = false;
        this.insideDoubleQuote = false;
        this.hasSingleNoNameParameter = params.size() == 1 && params.keySet().iterator().next().equals("");
        this.params = params;

        convertToQuestionMarks();

        valuesArray = values.toArray();
    }

    public String getQuestionMarksQuery() {
        return questionMarksQuery.toString();
    }

    public String getNamedParamsQuery() {
        return namedParamsQuery;
    }

    public Object[] getQuestionMarksValues() {
        return valuesArray;
    }

    public Map<String, Object> effectiveParams() {
        if (hasSingleNoNameParameter) {
            return Collections.singletonMap(uniqueParamNames.iterator().next(), params.values().iterator().next());
        }

        return params;
    }

    public boolean isEmpty() {
        return params.isEmpty();
    }

    static Map<String, Object> singleNoNameParam(Object singleParam) {
        return Collections.singletonMap("", singleParam);
    }

    private void convertToQuestionMarks() {
        char c;

        for (int idx = 0; idx < namedParamsQuery.length(); idx++) {
            c = namedParamsQuery.charAt(idx);

            if (c == '\'' && !insideDoubleQuote) {
                insideSingleQuote = !insideSingleQuote;
            }

            if (c == '"' && !insideSingleQuote) {
                insideDoubleQuote = !insideDoubleQuote;
            }

            if (c == ':' && !insideSingleQuote && !insideDoubleQuote) {
                insideParamName = true;
                continue;
            }

            if (insideParamName &&
                    (Character.isAlphabetic(c) || (Character.isDigit(c) && currentNamedParam.length() > 0))) {
                currentNamedParam.append(c);
            } else if (insideParamName) {
                handleCurrentParamName();
                questionMarksQuery.append(c);
            } else {
                questionMarksQuery.append(c);
            }
        }

        if (insideParamName && currentNamedParam.length() > 0) {
            handleCurrentParamName();
        }
    }

    private void handleCurrentParamName() {
        String paramName = currentNamedParam.toString();
        uniqueParamNames.add(paramName);

        Object valueToAppend = valueByName(paramName);
        if (valueToAppend instanceof Iterable) {
            handleIterableParam((Iterable<?>) valueToAppend);
        } else {
            handleSingleParam(valueToAppend);
        }

        insideParamName = false;
        currentNamedParam.setLength(0);
    }

    private void handleIterableParam(Iterable<?> valuesToAppend) {
        Iterator<?> it = valuesToAppend.iterator();
        while (it.hasNext()) {
            Object v = it.next();
            values.add(v);
            questionMarksQuery.append('?');

            if (it.hasNext()) {
                questionMarksQuery.append(", ");
            }
        }
    }

    private void handleSingleParam(Object valueToAppend) {
        values.add(valueToAppend);
        questionMarksQuery.append('?');
    }

    private Object valueByName(String name) {
        if (hasSingleNoNameParameter) {
            if (uniqueParamNames.size() > 1) {
                throw new IllegalArgumentException("one no-name parameter value was provided, " +
                        "but have seen multiple placeholders: " + uniqueParamNames);
            }

            return params.values().iterator().next();
        }

        Object value = params.get(name);
        if (value == null) {
            throw new IllegalArgumentException("No parameter value found: " + name);
        }

        return value;
    }
}
