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

class ConfigParserPersonaSelectDelegate {
    private static final String USAGE = "usage for personas should look like this:\n" +
            "personas {\n" +
            "   Alice {\n" +
            "     email = \"alice-email\"\n" +
            "   }\n" +
            "}\n"

    private final ConfigValueHolder root
    private final ValuesPerPersona valuesPerPersona
    private final ValuesPerPersona valuesPerPersonaRoot

    ConfigParserPersonaSelectDelegate(ConfigValueHolder root,
                                      ValuesPerPersona valuesPerPersona,
                                      ValuesPerPersona valuesPerPersonaRoot) {
        this.root = root
        this.valuesPerPersona = valuesPerPersona
        this.valuesPerPersonaRoot = valuesPerPersonaRoot
    }

    def invokeMethod(String personaName, args) {
        if (args.length != 1 || !(args[0] instanceof Closure)) {
            throw new IllegalArgumentException(USAGE)
        }

        // multiple roots scenario:
        // one root is the actual config/env root
        // another root is the persona values
        // e.g.
        //
        // personas { Alice { email = 'alice-email' }}
        // environments { dev { personas { Alice { email = 'alice-dev-email' } } } }
        //
        def roots = [root]
        def personaRoot = valuesPerPersonaRoot.get(personaName)
        if (personaRoot) {
            roots.add(0, personaRoot)
        }

        def personaValueHolder = valuesPerPersona.createOrFindPersonaValueHolderById(personaName, roots)
        def delegate = new ConfigParserValueHolderDelegate(personaValueHolder)

        Closure definitionClosure = DslUtils.closureCopyWithDelegate(args[0], delegate)
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
