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
import org.testingisdocumenting.webtau.db.cfg.DbDataSourceProviders;

import javax.sql.DataSource;

public class DatabaseFacade {
    public static final DatabaseFacade db = new DatabaseFacade();

    private DatabaseFacade() {
    }

    public Database from(DataSource dataSource) {
        return new Database(dataSource);
    }

    public DatabaseTable table(String tableName) {
        return from(PrimaryDataSourceHolder.dataSource).table(tableName);
    }

    public TableData query(String query) {
        return from(PrimaryDataSourceHolder.dataSource).query(query);
    }

    public void update(String query) {
        from(PrimaryDataSourceHolder.dataSource).update(query);
    }

    private static class PrimaryDataSourceHolder {
        static final DataSource dataSource = DbDataSourceProviders.provideByName("primary");
    }
}
