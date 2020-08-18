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

import org.junit.Test;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.WebTauCore.persona;

public class PersonaTest {
    @Test
    public void shouldTrackCurrentlyActivePersona() {
        Persona John = persona("John");

        actual(Persona.getCurrentPersona()).should(equal(null));

        John.execute(() -> {
            actual(Persona.getCurrentPersona().getId()).should(equal("John"));
        });

        actual(Persona.getCurrentPersona()).should(equal(null));
    }

    @Test
    public void shouldLetReturnValueFromPersonaContext() {
        Persona John = persona("John");

        String message = John.execute(() -> {
            actual(Persona.getCurrentPersona().getId()).should(equal("John"));
            return "hello";
        });

        actual(message).should(equal("hello"));
    }

    @Test
    public void shouldNotAllowNestingPersonas() {
        Persona John = persona("John");
        Persona Bob = persona("Bob");

        code(() -> {
            John.execute(() -> {
                Bob.execute(() -> {

                });
            });
        }).should(throwException("nesting personas is not allowed, active persona id: John, " +
                "attempted to nest persona id: Bob"));
    }
}