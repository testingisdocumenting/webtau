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

class ConfigParserDslDelegate extends ConfigParserValueHolderDelegate {
    public final Map<String, ConfigValueHolder> valuesPerEnv = new LinkedHashMap<>()
    public final ConfigParserPersonaValues personaValues = new ConfigParserPersonaValues()

    public final ConfigParserEnvironmentSelectDelegate environmentSelectDelegate
    public final ConfigParserPersonaSelectDelegate personaSelectDelegate

    ConfigParserDslDelegate() {
        super(ConfigValueHolder.withNameOnly("cfg"))
        environmentSelectDelegate = new ConfigParserEnvironmentSelectDelegate(this.@root, valuesPerEnv, personaValues)
        personaSelectDelegate = new ConfigParserPersonaSelectDelegate(this.@root, personaValues.valuesPerPersona, new ValuesPerPersona())
    }

    void environments(Closure setup) {
        def dslDefinition = DslUtils.closureCopyWithDelegate(setup, environmentSelectDelegate)
        dslDefinition.run()
    }

    void personas(Closure setup) {
        def dslDefinition = DslUtils.closureCopyWithDelegate(setup, personaSelectDelegate)
        dslDefinition.run()
    }

    Map<String, Object> envValuesToMap(String env) {
        return this.@environmentSelectDelegate.@valuesPerEnv.get(env).toMap()
    }

    Map<String, Object> combinedValuesForEnv(String env) {
        def result = new LinkedHashMap<>(this.@root.toMap())
        result.putAll(valuesPerEnv.get(env).toMap())

        return result
    }

    Map<String, Object> personaValuesToMap(String personaId) {
        return personaValues.valuesPerPersona.get(personaId).toMap()
    }

    Map<String, Object> envPersonaValuesToMap(String env, String personaId) {
        def result = combinedValuesForEnv(env)
        def personaRootValue = this.@personaValues.valuesPerPersona.get(personaId)
        if (personaRootValue) {
            result.putAll(personaRootValue.toMap())
        }

        def personasEnvRootValue = this.@personaValues.valuesPerEnvPerPersona.get(env)
        if (personasEnvRootValue) {
            def personaEnvRootValue = personasEnvRootValue.get(personaId)
            if (personaEnvRootValue) {
                result.putAll(personaEnvRootValue.toMap())
            }
        }

        return result
    }
}
