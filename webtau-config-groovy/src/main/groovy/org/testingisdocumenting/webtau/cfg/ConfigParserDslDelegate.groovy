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
    public final ConfigParserEnvironmentsDelegate environmentsDelegate

    ConfigParserDslDelegate() {
        super(ConfigValueHolder.withNameOnly("cfg"))
        environmentsDelegate = new ConfigParserEnvironmentsDelegate(this.@root)
    }

    void environments(Closure setup) {
        def cloned = setup.clone() as Closure
        cloned.delegate = environmentsDelegate
        cloned.resolveStrategy = Closure.DELEGATE_FIRST
        cloned.run()
    }

    Map<String, Object> envValuesToMap(String env) {
       return this.@environmentsDelegate.@valuesPerEnv.get(env).toMap()
    }

    Map<String, Object> personaValuesToMap(String personaId) {
       return this.@root.@valuePerPersona.get(personaId).convertToMap()
    }

    Map<String, Object> envPersonaValuesToMap(String env, String personaId) {
        return this.@environmentsDelegate.@valuesPerEnv.get(env).@valuePerPersona.get(personaId).toMap()
    }
}
