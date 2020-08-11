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

package org.testingisdocumenting.webtau.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testingisdocumenting.webtau.utils.json.JsonSerializationModuleProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final List<JsonSerializationModuleProvider> moduleProviders =
            ServiceLoaderUtils.load(JsonSerializationModuleProvider.class);

    private static final ObjectMapper mapper = createMapper();

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        moduleProviders.stream()
                .map(JsonSerializationModuleProvider::provide)
                .forEach(mapper::registerModule);

        return mapper;
    }

    private JsonUtils() {
    }

    public static String serialize(Object json) {
        if (json == null) {
            return "null";
        }

        try {
            return mapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] serializeToBytes(Object json) {
        if (json == null) {
            return "null".getBytes();
        }

        try {
            return mapper.writeValueAsBytes(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String serializePrettyPrint(Object json) {
        if (json == null) {
            return "null";
        }

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ?> deserializeAsMap(String json) {
        if (json == null) {
            return null;
        }

        try {
            return mapper.readValue(json, Map.class);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    public static Map<String, ?> deserializeAsMap(Path file) {
        return deserializeAsMap(FileUtils.fileTextContent(file));
    }

    public static List<?> deserializeAsList(String json) {
        if (json == null) {
            return null;
        }

        try {
            return mapper.readValue(json, List.class);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    public static List<?> deserializeAsList(Path file) {
        return deserializeAsList(FileUtils.fileTextContent(file));
    }

    public static Object deserialize(String json) {
        if (json == null) {
            return null;
        }

        try {
            return mapper.readValue(json, Object.class);
        } catch (IOException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    public static JsonNode convertToTree(Object object) {
        return mapper.valueToTree(object);
    }

    public static <T> T convert(Object fromValue, Class<T> toValueType) {
        return mapper.convertValue(fromValue, toValueType);
    }
}
