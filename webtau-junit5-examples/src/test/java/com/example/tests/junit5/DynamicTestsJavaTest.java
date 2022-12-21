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
import org.testingisdocumenting.webtau.junit5.WebTau;

import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;

@WebTau
class DynamicTestsJavaTest {
    @TestFactory
    public Stream<DynamicTest> individualTestsUseGeneratedDisplayLabels() {
        TableData useCases = table("price", "quantity", "outcome",
                                   ______________________________,
                                     10.0 ,         30,     300.0,
                                    -10.0 ,         30,    -300.0);

        return DynamicTests.fromTable(useCases, r -> {
            double price = r.get("price");
            int quantity = r.get("quantity");
            double outcome = r.get("outcome");

            actual(PriceCalculator.calculate(price, quantity)).should(equal(outcome));
        });
    }

    @TestFactory
    public Stream<DynamicTest> individualTestsLabelToClarifyUseCase() {
        TableData useCases = table("label"         , "price", "quantity", "outcome",
                                  _________________________________________________,
                                   "positive price",   10.0 ,         30,     300.0,
                                   "negative price",  -10.0 ,         30,    -300.0);

        return DynamicTests.fromTable(useCases, r -> {
            double price = r.get("price");
            int quantity = r.get("quantity");
            double outcome = r.get("outcome");

            actual(PriceCalculator.calculate(price, quantity)).should(equal(outcome));
        });
    }
}
