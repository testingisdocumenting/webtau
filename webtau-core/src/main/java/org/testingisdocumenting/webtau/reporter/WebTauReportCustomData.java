/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.reporter;

import java.util.Collections;
import java.util.Map;

public class WebTauReportCustomData {
    private final String id;
    private final Object data;

    public WebTauReportCustomData(String id, Object data) {
        this.id = id;
        this.data = data;
    }

    public Map<String, ?> toMap() {
        return Collections.singletonMap(id, data);
    }

    public String getId() {
        return id;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ReportCustomData{" +
                "id='" + id + '\'' +
                ", data=" + data +
                '}';
    }
}
