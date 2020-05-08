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
import org.junit.Test

import javax.sql.DataSource

import static org.testingisdocumenting.webtau.db.DatabaseFacade.db

class DatabaseFacadeTest {
    public static DbDataSourceProvider h2PrimaryProvider = new H2PrimaryDbDataSourceProvider()

    @BeforeClass
    static void init() {
        DbDataSourceProviders.add(h2PrimaryProvider)

        JdbcDataSource dataSource = createDataSource()
        createPricesTable(dataSource)
    }

    @AfterClass
    static void cleanup() {
        DbDataSourceProviders.remove(h2PrimaryProvider)
    }

    @Test
    void "should insert table data into a table"() {
        JdbcDataSource dataSource = createDataSource()

        def database = db.from(dataSource)
        def PRICES = database.table("PRICES")

        PRICES << ["id" | "description" | "price"] {
                  ___________________________________
                  "id1" | "nice set"    | 1000
                  "id2" | "another set" | 2000 }

        PRICES.query().should == ["ID" | "DESCRIPTION" | "PRICE"] {
                                 ___________________________________
                                 "id1" | "nice set"    | 1000
                                 "id2" | "another set" | 2000 }

        db.query("select * from PRICES where id='id2'").should ==
                ["ID" | "DESCRIPTION" | "PRICE"] {
                __________________________________
                "id2" | "another set" | 2000 }
    }

    @Test
    void "should use data source provider for primary database"() {
        db.update("delete from PRICES")

        def PRICES = db.table("PRICES")
        PRICES << ["id" | "description" | "price"] {
                  ___________________________________
                  "id2" | "another set" | 2000   }

        PRICES.query().numberOfRows().should == 1
    }

    @Test
    void "should run execute statements for primary data source"() {
        def PRICES = db.table("PRICES")
        PRICES << ["id" | "description" | "price"] {
                  ___________________________________
                  "id2" | "another set" | 2000   }

        db.update("delete from PRICES")

        PRICES.query().numberOfRows().should == 0
    }

    private static JdbcDataSource createDataSource() {
        def dataSource = new JdbcDataSource()
        dataSource.setURL("jdbc:h2:mem:dbfence;DB_CLOSE_DELAY=-1")
        dataSource.setUser("sa")
        dataSource
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
