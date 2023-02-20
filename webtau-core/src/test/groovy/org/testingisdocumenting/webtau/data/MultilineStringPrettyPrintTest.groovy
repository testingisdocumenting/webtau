/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.data

import org.junit.Test
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.data.render.PrettyPrintableTestBase
import org.testingisdocumenting.webtau.data.render.PrettyPrinterDecorationToken
import org.testingisdocumenting.webtau.data.render.StringPrettyPrintable

class MultilineStringPrettyPrintTest extends PrettyPrintableTestBase {
    @Test
    void "single line"() {
        def prettyPrintable = new StringPrettyPrintable(new MultilineString("single line"))
        prettyPrintable.prettyPrint(printer)

        expectOutput('"single line"')
    }

    @Test
    void "single line decorated"() {
        def prettyPrintable = new StringPrettyPrintable(new MultilineString("single line"))
        prettyPrintable.prettyPrint(printer, ValuePath.UNDEFINED, new PrettyPrinterDecorationToken('---', Color.BLUE))

        expectOutput('---"single line"---')
    }

    @Test
    void "multi line"() {
        def prettyPrintable = new StringPrettyPrintable(new MultilineString("hello\nworld"))
        prettyPrintable.prettyPrint(printer)

        expectOutput('_____\n' +
                'hello\n' +
                'world\n' +
                '_____')
    }

    @Test
    void "multi line single line decorated"() {
        def text = new MultilineString("hello\nworld")
        text.setFirstFailedLineIdx(1)

        def prettyPrintable = new StringPrettyPrintable(text)
        prettyPrintable.prettyPrint(printer, ValuePath.UNDEFINED, new PrettyPrinterDecorationToken("&&", Color.RED))

        expectOutput('  _____\n' +
                '  hello\n' +
                '&&world&&\n' +
                '  _____')
    }

    @Test
    void "whole multiline block decorated"() {
        def text = new MultilineString("hello\nworld")

        def prettyPrintable = new StringPrettyPrintable(text)
        prettyPrintable.prettyPrint(printer, ValuePath.UNDEFINED, new PrettyPrinterDecorationToken("**", Color.RED))

        expectOutput('**_____**\n' +
                '  hello\n' +
                '  world\n' +
                '**_____**')
    }
}
