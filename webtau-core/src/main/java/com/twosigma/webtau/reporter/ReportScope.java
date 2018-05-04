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

package com.twosigma.webtau.reporter;

import java.util.ArrayList;
import java.util.List;

public class ReportScope {
    private String id;
    private List<ReportToken> report;

    private List<ReportScope> children;

    public ReportScope(String id) {
        this.id = id;
        this.report = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public void addToken(String type, String textRepresentation) {
        report.add(new ReportToken(type, textRepresentation));
    }

    public void addScope(ReportScope scope) {
        children.add(scope);
    }
}
