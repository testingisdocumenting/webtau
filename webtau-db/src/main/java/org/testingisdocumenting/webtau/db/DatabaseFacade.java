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
import java.util.Map;

public class DatabaseFacade {
    private static final LabeledDataSourceCachedProvider primaryDataSourceProvider =
            new LabeledDataSourceCachedProvider(
                    () -> new LabeledDataSource(DbDataSourceProviders.provideByName("primary"), "primary-db"));

    public static final DatabaseFacade db = new DatabaseFacade();

    static void reset() {
        primaryDataSourceProvider.reset();
    }

    public Database from(DataSource dataSource, String label) {
        return from(new LabeledDataSourceCachedProvider(() -> new LabeledDataSource(dataSource, label)));
    }

    public Database from(LabeledDataSourceProvider labeledDataSourceProvider) {
        return new Database(labeledDataSourceProvider);
    }

    public DatabaseTable table(String tableName) {
        return from(primaryDataSourceProvider).table(tableName);
    }

    public DbQuery query(String query) {
        return from(primaryDataSourceProvider).query(query);
    }

    public DbQuery query(String query, Map<String, Object> params) {
        return from(primaryDataSourceProvider).query(query, params);
    }

    public <E> DbQuery query(String query, E singleParam) {
        return from(primaryDataSourceProvider).query(query, singleParam);
    }

    public TableData queryTableData(String query) {
        return from(primaryDataSourceProvider).queryTableData(query);
    }

    public TableData queryTableData(String query, Map<String, Object> params) {
        return from(primaryDataSourceProvider).queryTableData(query, params);
    }

    public <E> E querySingleValue(String query) {
        return from(primaryDataSourceProvider).querySingleValue(query);
    }

    public <E> E querySingleValue(String query, Map<String, Object> params) {
        return from(primaryDataSourceProvider).querySingleValue(query, params);
    }

    public void update(String query) {
        from(primaryDataSourceProvider).update(query);
    }

    public void update(String query, Map<String, Object> params) {
        from(primaryDataSourceProvider).update(query, params);
    }

    public <E> void update(String query, E singleParam) {
        from(primaryDataSourceProvider).update(query, singleParam);
    }
}
