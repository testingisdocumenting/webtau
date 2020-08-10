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

package org.testingisdocumenting.webtau.db;

import javax.sql.DataSource;

public class LabeledDataSource {
    private final DataSource dataSource;
    private final String label;

    public LabeledDataSource(DataSource dataSource, String label) {
        this.dataSource = dataSource;
        this.label = label;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public String getLabel() {
        return label;
    }
}
