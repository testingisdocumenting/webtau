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

package org.testingisdocumenting.webtau.db.cfg;

import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DbDataSourceProviders {
    private static final List<DbDataSourceProvider> providers = ServiceLoaderUtils.load(DbDataSourceProvider.class);
    private static final ConcurrentHashMap<String, DataSource> dataSourcesByName = new ConcurrentHashMap<>();

    public static DataSource provideByName(String name) {
        DataSource existing = dataSourcesByName.get(name);
        if (existing != null) {
            return existing;
        }

        DataSource created = createDataSource(name);
        dataSourcesByName.put(name, created);

        return created;
    }

    private static DataSource createDataSource(String name) {
        return providers.stream()
                .map(p -> p.provide(name))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> noProviderFound(name));
    }

    private static RuntimeException noProviderFound(String name) {
        return new RuntimeException("no DbDataSourceProvider found for " + name + " database");
    }
}
