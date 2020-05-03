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
import org.junit.Test

import javax.sql.DataSource

import static org.testingisdocumenting.webtau.db.DatabaseFacade.db

class DatabaseFacadeTest {
    @Test
    void "should insert table data into a table"() {
        def dataSource = new JdbcDataSource()
        dataSource.setURL("jdbc:h2:mem:dbfence;DB_CLOSE_DELAY=-1")
        dataSource.setUser("sa")

        createPricesTable(dataSource)

        def database = db.from(dataSource)
        def PRICES = database.table("PRICES")

        PRICES << ["id" | "description" | "price"] {
                  ___________________________________
                  "id1" | "nice set"    | 1000
                  "id2" | "another set" | 2000 }

        PRICES.load().should == ["ID" | "DESCRIPTION" | "PRICE"] {
                                ___________________________________
                                "id1" | "nice set"    | 1000
                                "id2" | "another set" | 2000 }
    }

    private static void createPricesTable(DataSource dataSource) {
        String definition =
"""CREATE TABLE PRICES (
    id varchar(255),
    description varchar(255),
    price int
);
"""
        def run = new QueryRunner(dataSource)
        run.update(definition)
    }
}
