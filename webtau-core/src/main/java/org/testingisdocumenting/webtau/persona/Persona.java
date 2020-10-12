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

package org.testingisdocumenting.webtau.persona;

import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

public class Persona {
    private static final Persona defaultPersona = new Persona("", Collections.emptyMap(), true);
    private static final ThreadLocal<Persona> currentPersona = ThreadLocal.withInitial(() -> defaultPersona);

    private final String id;
    private final Map<String, Object> payload;
    private final boolean isDefault;

    public Persona(String id) {
        this(id, Collections.emptyMap());
    }

    public Persona(String id, Map<String, Object> payload) {
        this(id, payload, false);
    }

    private Persona(String id, Map<String, Object> payload, boolean isDefault) {
        if (!isDefault && StringUtils.nullOrEmpty(id)) {
            throw new IllegalArgumentException("Persona id may not be null or empty");
        }
        this.id = id;
        this.payload = payload;
        this.isDefault = isDefault;
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void execute(Runnable code) {
        execute(() -> {
            code.run();
            return null;
        });
    }

    public <R> R execute(Supplier<R> code) {
        Persona current = currentPersona.get();
        if (current != defaultPersona) {
            throw new IllegalStateException("nesting personas is not allowed, active persona id: " + current.id +
                    ", attempted to nest persona id: " + id);
        }

        currentPersona.set(this);
        try {
            return code.get();
        } finally {
            currentPersona.set(defaultPersona);
        }
    }

    public static Persona getCurrentPersona() {
        return currentPersona.get();
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id='" + id + '\'' +
                ", payload=" + payload +
                '}';
    }
}
