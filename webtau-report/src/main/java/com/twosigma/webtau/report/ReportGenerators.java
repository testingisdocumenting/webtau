/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.report;

import com.twosigma.webtau.utils.ServiceLoaderUtils;

import java.util.Set;

public class ReportGenerators {
    private static final Set<ReportGenerator> generators = ServiceLoaderUtils.load(ReportGenerator.class);

    public static void generate(Report report) {
        generators.forEach(g -> g.generate(report));
    }

    public static void add(ReportGenerator reportGenerator) {
        generators.add(reportGenerator);
    }

    public static void remove(ReportGenerator reportGenerator) {
        generators.remove(reportGenerator);
    }
}
