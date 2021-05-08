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

package org.testingisdocumenting.webtau.persona

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class PersonaExtensionsTest {
    @Test
    void "should support implicit closure call"() {
        def traces = []
        def Bob = Persona.persona("Bob")
        def John = Persona.persona("John")

        John {
            Persona.currentPersona.id.should == "John"
            traces.add(Persona.currentPersona.id)
        }

        Bob {
            Persona.currentPersona.id.should == "Bob"
            traces.add(Persona.currentPersona.id)
        }

        traces.should == ["John", "Bob"]
    }

    @Test
    void "should let return value from persona context"() {
        def John = Persona.persona("John")

        String message = John {
            Persona.currentPersona.id.should == "John"
            return "hello"
        }

        message.should == "hello"
    }

    @Test
    void "nesting of personas should not be allowed"() {
        def Bob = Persona.persona("Bob")
        def John = Persona.persona("John")

        code {
            John {
                Bob {
                    Persona.currentPersona.id.should == "John"
                }
            }
        } should throwException("nesting personas is not allowed, active persona id: John, " +
                "attempted to nest persona id: Bob")
    }

    @Test
    void "nesting of same persona is allowed"() {
        def John = Persona.persona("John")
        John {
            John {
                Persona.currentPersona.id.should == "John"
            }
        }
    }
}
