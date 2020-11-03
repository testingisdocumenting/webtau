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

package com.example.tests.junit5;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.junit5.DynamicTests;

import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;

class DynamicTestsJavaTest {
    TableData useCases = table("price", "quantity", "outcome",
                               ______________________________,
                                   10 ,         30,       300,
                                  -10 ,         30,      -300);
    @TestFactory
    public Stream<DynamicTest> individualTestsUseGeneratedDisplayLabels() {
        return DynamicTests.fromTable(useCases, r -> {
            long price = r.get("price");
            long quantity = r.get("quantity");
            long outcome = r.get("outcome");

            actual(PriceCalculator.calculate(price, quantity)).should(equal(outcome));
        });
    }
}
