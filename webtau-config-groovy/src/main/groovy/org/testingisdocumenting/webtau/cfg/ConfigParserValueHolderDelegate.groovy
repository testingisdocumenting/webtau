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
    private static final String NESTED_PROP_USAGE = "usage for nested properties:\n" +
            "myComplexConfigValue {\n" +
            "   nestedName = \"value\"\n" +
            "}\n"

    protected final ConfigValueHolder root

    ConfigParserValueHolderDelegate(ConfigValueHolder root) {
        this.root = root
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

    def invokeMethod(String parentProp, args) {
        if (parentProp === "environments") {
            // TODO
        }

        if (args.length != 1 || !(args[0] instanceof Closure)) {
            throw new IllegalArgumentException(NESTED_PROP_USAGE)
        }

        def newRoot = this.root.getProperty(parentProp)
        def delegate = new ConfigParserValueHolderDelegate(newRoot)
        Closure definitionClosure = DslUtils.closureCopyWithDelegate(args[0], delegate)
        definitionClosure.run()
    }
}
