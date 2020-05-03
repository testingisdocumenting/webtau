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

package org.testingisdocumenting.webtau.db

import org.h2.jdbcx.JdbcDataSource
import org.testingisdocumenting.webtau.db.cfg.DbDataSourceProvider

import javax.sql.DataSource

class H2PrimaryDbDataSourceProvider implements DbDataSourceProvider {
    @Override
    DataSource provide(String name) {
        if (name != 'primary') {
            return null
        }

        def dataSource = new JdbcDataSource()
        dataSource.setURL("jdbc:h2:mem:dbfence;DB_CLOSE_DELAY=-1")
        dataSource.setUser("sa")

        return dataSource;
    }
}
