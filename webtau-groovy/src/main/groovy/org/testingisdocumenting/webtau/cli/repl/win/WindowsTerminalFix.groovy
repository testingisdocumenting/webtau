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

package org.testingisdocumenting.webtau.cli.repl.win

import groovy.transform.InheritConstructors
import jline.TerminalFactory
import jline.WindowsTerminal
import org.codehaus.groovy.tools.shell.util.WrappedInputStream

@InheritConstructors
class WindowsTerminalFix extends WindowsTerminal {
    @Override
    protected boolean isSystemIn(InputStream i) throws IOException {
        // groovysh wraps the input stream in WrappedInputStream, need to handle this case properly to enable windows controll character handling
        // https://issues.apache.org/jira/browse/GROOVY-6453
        if (i instanceof WrappedInputStream) {
            return super.isSystemIn(((WrappedInputStream) i).wrapped)
        }
        return super.isSystemIn(i)
    }

    static void registerIfRequired() {
        if (System.getProperty("jline.terminal") != null) {
            return
        }

        TerminalFactory.registerFlavor(TerminalFactory.Flavor.WINDOWS, WindowsTerminalFix.class)
        System.setProperty("jline.terminal", "win")
    }
}
