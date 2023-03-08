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

package org.testingisdocumenting.webtau.app.cfg

class ConfigParserEnvironmentDelegate extends ConfigParserValueHolderDelegate {
    private final ConfigParserPersonaValues personaValues
    private final String envName

    ConfigParserEnvironmentDelegate(String envName,
                                    ConfigValueHolder envRoot,
                                    ConfigParserPersonaValues personaValues) {
        super(envRoot)
        this.envName = envName
        this.personaValues = personaValues
    }

    void personas(Closure setup) {
        def valuesPerPersonaForEnv = this.@personaValues.createOrFindValuesPerPersonaForEnv(this.@envName)
        def delegate = new ConfigParserPersonaSelectDelegate(this.@root,
                valuesPerPersonaForEnv,
                this.@personaValues.@valuesPerPersona)

        def dslDefinition = DslUtils.closureCopyWithDelegate(setup, delegate)
        dslDefinition.run()
    }
}
