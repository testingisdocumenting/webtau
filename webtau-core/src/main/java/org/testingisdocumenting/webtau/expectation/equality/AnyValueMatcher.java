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

package org.testingisdocumenting.webtau.expectation.equality;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class AnyValueMatcher implements ValueMatcher, PrettyPrintable {
    @Override
    public TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("to match any value");
    }

    @Override
    public TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("matches every value");
    }

    @Override
    public TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        throw new IllegalStateException();
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actual) {
        return true;
    }

    @Override
    public TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("to mismatch any value");
    }

    @Override
    public TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        throw new IllegalStateException();
    }

    @Override
    public TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().id("anyValue").matcher("matches every value");
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actual) {
        return false;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        printer.printDelimiter("<");
        printer.print(PrettyPrinter.CLASSIFIER_COLOR, "any value");
        printer.printDelimiter(">");
    }

    @Override
    public String toString() {
        return PrettyPrinter.renderAsTextWithoutColors(this);
    }
}
