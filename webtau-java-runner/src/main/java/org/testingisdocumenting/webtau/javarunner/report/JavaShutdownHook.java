/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.javarunner.report;

import org.testingisdocumenting.webtau.report.ReportGenerators;
import org.testingisdocumenting.webtau.TestListeners;

public class JavaShutdownHook {
    public final static JavaShutdownHook INSTANCE = new JavaShutdownHook();

    private JavaShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            TestListeners.afterAllTests();

            JavaReport javaReport = JavaReport.INSTANCE;

            javaReport.stopTimer();
            ReportGenerators.generate(javaReport.create());
        }));
    }

    public void noOp() {
    }
}
