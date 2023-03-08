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

import org.apache.commons.dbutils.QueryRunner
import org.h2.jdbcx.JdbcDataSource
import org.junit.AfterClass
import org.junit.BeforeClass

import javax.sql.DataSource

import static org.testingisdocumenting.webtau.WebTauCore.*
import static Database.db

class DatabaseBaseTest {
    private static boolean areTablesCreated
    protected static DbDataSourceProvider h2PrimaryProvider = new H2PrimaryDbDataSourceProvider()
    protected static DataSource h2DataSource

    @BeforeClass
    static void init() {
        DbDataSourceProviders.add(h2PrimaryProvider)

        h2DataSource = createDataSource()
        createPricesTable(h2DataSource)
    }

    @AfterClass
    static void cleanup() {
        DbDataSourceProviders.remove(h2PrimaryProvider)
    }

    private static JdbcDataSource createDataSource() {
        def dataSource = new JdbcDataSource()
        dataSource.setURL("jdbc:h2:mem:dbfence;DB_CLOSE_DELAY=-1")
        dataSource.setUser("sa")

        return dataSource
    }

    static def setupPrices() {
        db.update("delete from PRICES")
        def PRICES = db.table("PRICES")
        PRICES << ["id" | "description" | "price"] {
                  __________________________________
                  "id1" | "nice set"    | 1000
                  "id2" | "another set" | 2000 }

        return PRICES
    }

    private static void createPricesTable(DataSource dataSource) {
        if (areTablesCreated) {
            return
        }

        String definition =
                """CREATE TABLE PRICES (
    id varchar(255),
    external_id varchar(255),
    description varchar(255),
    available bool,
    type varchar(255),
    price int
);
"""
        def run = new QueryRunner(dataSource)
        run.update(definition)

        areTablesCreated = true
    }
}
