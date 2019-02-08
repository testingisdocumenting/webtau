/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.documentation

import com.twosigma.documentation.DocumentationArtifacts
import com.twosigma.webtau.data.table.TableData
import org.junit.Test

class TestToDocExample {
    @Test
    void shouldRestrictAccountsActivity() {
        def rules = ["Account Type" | "Operation"    | "Restriction"] {
                    _________________________________________________
                        "SPB3"      | "Buy Options"  | "weekends only"
                        "TR"        | "Sell Futures" | "except holidays"
                        "BOSS"      | "Buy Stocks"   | "none" }

        validateRules(rules)
    }

    private static void validateRules(TableData rules) {
        DocumentationArtifacts.create(TestToDocExample,
                "account-rules.json", rules.toJson())
    }
}
