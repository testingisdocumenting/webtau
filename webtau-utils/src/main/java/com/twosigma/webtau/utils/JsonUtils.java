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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {
    }

    public static String serialize(Object json) {
        if (json == null) {
            return null;
        }

        try {
            return mapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    public static String serializePrettyPrint(Object json) {
        if (json == null) {
            return null;
        }

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ?> deserializeAsMap(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, Map.class);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<?> deserializeAsList(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, List.class);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    public static Object deserialize(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, Object.class);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }
}
