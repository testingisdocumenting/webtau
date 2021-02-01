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

import org.testingisdocumenting.webtau.data.table.TableData;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

public class DatabaseFacade {
    public static final DatabaseFacade db = new DatabaseFacade();

    private DatabaseFacade() {
    }

    public Database from(DataSource dataSource, String label) {
        return new Database(new LabeledDataSource(dataSource, label));
    }

    public Database from(LabeledDataSource labeledDataSource) {
        return new Database(labeledDataSource);
    }

    public DatabaseTable table(String tableName) {
        return from(getPrimaryDataSource()).table(tableName);
    }

    public DbQuery createQuery(String query) {
        return from(getPrimaryDataSource()).createQuery(query);
    }

    public DbQuery createQuery(String query, Map<String, Object> params) {
        return from(getPrimaryDataSource()).createQuery(query, params);
    }

    public TableData queryTableData(String query) {
        return from(getPrimaryDataSource()).queryTableData(query);
    }

    public TableData queryTableData(String query, Map<String, Object> params) {
        return from(getPrimaryDataSource()).queryTableData(query, params);
    }

    public <E> E querySingleValue(String query) {
        return from(getPrimaryDataSource()).querySingleValue(query);
    }

    public <E> E querySingleValue(String query, Map<String, Object> params) {
        return from(getPrimaryDataSource()).querySingleValue(query, params);
    }

    public void update(String query) {
        from(getPrimaryDataSource()).update(query);
    }

    public void update(String query, Map<String, Object> params) {
        from(getPrimaryDataSource()).update(query, params);
    }

    private static LabeledDataSource getPrimaryDataSource() {
        return new LabeledDataSource(DbDataSourceProviders.provideByName("primary"), "primary-db");
    }
}
