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

class ConfigParserPersonasDelegate {
    private static final String USAGE = "usage for personas should look like this:\n" +
            "personas {\n" +
            "   Alice {\n" +
            "     email = \"alice-email\"\n" +
            "   }\n" +
            "}\n"

    private final ConfigValueHolder root

    ConfigParserPersonasDelegate(ConfigValueHolder root) {
        this.root = root
    }

    def invokeMethod(String personaName, args) {
        if (args.length != 1 || !(args[0] instanceof Closure)) {
            throw new IllegalArgumentException(USAGE)
        }

        def personaValueHolder = ConfigValueHolder.withRootAndPersona(personaName, personaName, root)
        def delegate = new ConfigParserValueHolderDelegate(personaValueHolder)

        Closure definitionClosure = args[0].clone() as Closure
        definitionClosure.delegate = delegate
        definitionClosure.resolveStrategy = Closure.DELEGATE_FIRST
        definitionClosure.run()
    }

    def getProperty(String name) {
        throwNoPropsOutsideEnvs()
    }

    void setProperty(String name, Object value) {
        throwNoPropsOutsideEnvs()
    }

    private static void throwNoPropsOutsideEnvs() {
        throw new IllegalArgumentException("config values must be defined within a specific persona\n" + USAGE)
    }
}
