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

class ConfigParserEnvironmentSelectDelegate {
    private static final String USAGE = "usage for environments should look like this:\n" +
            "environments {\n" +
            "   dev {\n" +
            "   }\n" +
            "}\n"

    private final ConfigValueHolder root

    public final Map<String, ConfigValueHolder> valuesPerEnv
    public final ConfigParserPersonaValues personaValues

    ConfigParserEnvironmentSelectDelegate(ConfigValueHolder root,
                                          Map<String, ConfigValueHolder> valuesPerEnv,
                                          ConfigParserPersonaValues personaValues) {
        this.root = root
        this.valuesPerEnv = valuesPerEnv
        this.personaValues = personaValues
    }

    def invokeMethod(String envName, args) {
        if (args.length != 1 || !(args[0] instanceof Closure)) {
            throw new IllegalArgumentException(USAGE)
        }

        def envRoot = ConfigValueHolder.withRoots(envName, [root])
        def delegate = new ConfigParserEnvironmentDelegate(envName, envRoot, personaValues)
        valuesPerEnv.put(envName, envRoot)

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
        throw new IllegalArgumentException("config values must be defined within a specific environment\n" + USAGE)
    }
}
