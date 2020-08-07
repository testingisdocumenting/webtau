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

package org.testingisdocumenting.webtau.graphql;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class GraphQLOperation {
    private final String name;
    private final GraphQLOperationType type;

    GraphQLOperation(String name, GraphQLOperationType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public GraphQLOperationType getType() {
        return type;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", name);
        result.put("type", type.name().toLowerCase());

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphQLOperation that = (GraphQLOperation) o;
        return Objects.equals(name, that.name) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "GraphQLOperation{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
