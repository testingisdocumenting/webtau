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

class ConfigParserValueHolderDelegate {
    protected final ConfigValueHolder root
    private final ConfigParserPersonasDelegate personasDelegate

    ConfigParserValueHolderDelegate(ConfigValueHolder root) {
        this.root = root
        this.personasDelegate = new ConfigParserPersonasDelegate(this.@root)
    }

    void setProperty(String name, Object value) {
        this.root.setProperty(name, value)
    }

    Object getProperty(String name) {
        return this.root.getProperty(name)
    }

    Map<String, Object> toMap() {
        return this.root.toMap()
    }

    void personas(Closure setup) {
        def cloned = setup.clone() as Closure
        cloned.delegate = personasDelegate
        cloned.resolveStrategy = Closure.DELEGATE_FIRST
        cloned.run()
    }
}

/*

email = 'email'
cliEnv = [common: 'CMN']

personas {
  Alice {
    email = 'new email' // needs to have root.set('Alice', 'email', 'new email')
    cliEnv.SPECIFIC = 'alice specific' // root.get('cliEnv').set('Alice', 'SPECIFIC', 'alice specific')
  }
}

 */