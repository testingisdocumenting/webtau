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

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(MapOrList.class, new MapOrListDeserializer()).create();
    private static final Gson gsonPretty = new GsonBuilder().registerTypeAdapter(MapOrList.class, new MapOrListDeserializer()).setPrettyPrinting().create();

    private JsonUtils() {
    }

    public static String serialize(Object data) {
        return gson.toJson(data);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ?> deserializeAsMap(String data) {
        try {
            return gson.fromJson(data, Map.class);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException("error parsing " + data, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<?> deserializeAsList(String data) {
        return gson.fromJson(data, List.class);
    }

    public static Object deserialize(String data) {
        MapOrList mapOrList = gson.fromJson(data, MapOrList.class);

        return mapOrList.list != null ?
                mapOrList.list :
                mapOrList.map;
    }

    public static String serializePrettyPrint(Object data) {
        return gsonPretty.toJson(data);
    }

    private static class MapOrList {
        private Map map;
        private List list;
    }

    private static class MapOrListDeserializer implements JsonDeserializer<MapOrList> {
        @Override
        public MapOrList deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            MapOrList result = new MapOrList();
            if (jsonElement.isJsonArray()) {
                result.list = jsonDeserializationContext.deserialize(jsonElement, List.class);
            } else {
                result.map = jsonDeserializationContext.deserialize(jsonElement, Map.class);
            }

            return result;
        }
    }
}
