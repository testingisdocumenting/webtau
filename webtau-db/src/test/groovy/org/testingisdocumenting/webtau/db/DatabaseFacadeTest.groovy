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

import org.junit.Test

import static org.testingisdocumenting.webtau.db.DatabaseFacade.db

class DatabaseFacadeTest extends DatabaseBaseTest {
    @Test
    void "should insert table data into a table"() {
        def database = db.from(h2DataSource, 'h2db')

        db.update("delete from PRICES")
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

        PRICES.query().numberOfRows.should == 1
    }

    @Test
    void "query should be optional during comparison"() {
        db.update("delete from PRICES")
        def PRICES = db.table("PRICES")

        PRICES << ["id" | "description" | "price"] {
                  ___________________________________
                  "id1" | "nice set"    | 1000
                  "id2" | "another set" | 2000 }

        PRICES.should == ["ID" | "DESCRIPTION" | "PRICE"] {
                         ___________________________________
                         "id1" | "nice set"    | 1000
                         "id2" | "another set" | 2000 }
    }

    @Test
    void "should query single value"() {
        db.update("delete from PRICES")
        def PRICES = db.table("PRICES")

        PRICES << ["id" | "description" | "price"] {
                  ___________________________________
                  "id1" | "nice set"    | 1000
                  "id2" | "another set" | 2000 }

        def price = db.query("select price from PRICES where id='id1'")
        price.should == 1000
        price.shouldNot == 2000
    }

    @Test
    void "should query single value with params"() {
        db.update("delete from PRICES")
        def PRICES = db.table("PRICES")

        PRICES << ["id" | "description" | "price"] {
                  ___________________________________
                  "id1" | "nice set"    | 1000
                  "id2" | "another set" | 2000 }

        def price = db.query("select price from PRICES where id=:id", [id: 'id1'])
        price.should == 1000
        price.shouldNot == 2000
    }

    @Test
    void "should run updates with params"() {
        db.update("delete from PRICES")
        def PRICES = db.table("PRICES")

        PRICES << ["id" | "description" | "price"] {
                  ___________________________________
                  "id1" | "nice set"    | 1000
                  "id2" | "another set" | 2000 }

        db.update("update PRICES set price=:price where id=:id", [id: 'id2', price: 4000])

        PRICES.should == ["ID" | "DESCRIPTION" | "PRICE"] {
                         ___________________________________
                         "id1" | "nice set"    | 1000
                         "id2" | "another set" | 4000 }
    }

    @Test
    void "should run execute statements for primary data source"() {
        def PRICES = db.table("PRICES")
        PRICES << ["id" | "description" | "price"] {
                  ___________________________________
                  "id2" | "another set" | 2000   }

        db.update("delete from PRICES")

        PRICES.query().numberOfRows.should == 0
    }
}
