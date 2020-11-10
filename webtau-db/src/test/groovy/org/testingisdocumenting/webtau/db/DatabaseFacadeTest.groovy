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

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.data.Data.data
import static org.testingisdocumenting.webtau.db.DatabaseFacade.db

class DatabaseFacadeTest extends DatabaseBaseTest {
    @Test
    void "should insert table data into a table"() {
        def database = db.from(h2DataSource, 'h2db')

        db.update("delete from PRICES")
        def PRICES = database.table("PRICES")

        PRICES << [ "id" | "description" | "price"] {
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

        def PRICES = db.table("PRICES") // declare PRICES table
        PRICES << [ "id" | "description" | "price"] { // append two rows to PRICES
                   ___________________________________
                   "id1" | "nice set"    | 1000
                   "id2" | "another set" | 2000 }

        PRICES.query().numberOfRows.should == 2
    }

    @Test
    void "use table data permute, above and guid to generate rows"() {
        db.update("delete from PRICES")

        def PRICES = db.table("PRICES")
        PRICES << [     "id" | "description" |          "available" |                "type" |       "price" ] {
                   _____________________________________________________________________________________________
                   cell.guid | "nice set"    |                 true |                "card" |            1000 // cell.guid generates random guid that can be used for ids
                   cell.guid | "nice set"    |                 true |                "card" | cell.above + 10 // cell.above refers values above and can be modified with simple math operations
                   cell.guid | "another set" | permute(true, false) | permute("rts", "fps") | cell.above + 20 } // permute generates additional rows generating new rows with all the permutations

        doc.capture(DatabaseFacadeTest, 'db-setup-permute-table', PRICES.query().tableData)
        PRICES.query().numberOfRows.should == 6
    }

    @Test
    void "use csv read data"() {
        db.update("delete from PRICES")

        def PRICES = db.table("PRICES")
        PRICES << data.csv.table('prices-db.csv')

        doc.capture(DatabaseFacadeTest, 'db-setup-csv-table', PRICES.query().tableData)
        PRICES.should == [ "*ID" | "DESCRIPTION"  | "AVAILABLE" | "TYPE" | "PRICE"] {
                          __________________________________________________________
                           "id1" | "description1" |        true | "card" | 200
                           "id2" | "description2" |       false |  "rts" | 400 }
    }

    @Test
    void "query should be optional during comparison"() {
        db.update("delete from PRICES")
        db.table("PRICES") << ["id" | "description" | "price"] {
                                         ___________________________________
                                         "id1" | "nice set"    | 1000
                                         "id2" | "another set" | 2000 }

        // validation mark for docs
        def PRICES = db.table("PRICES")
        PRICES.should == ["ID" | "DESCRIPTION" | "PRICE"] {
                         ___________________________________
                         "id1" | "nice set"    | 1000
                         "id2" | "another set" | 2000 }
    }

    @Test
    void "delete with params"() {
        db.update("delete from PRICES") // delete all

        def PRICES = db.table("PRICES")
        PRICES << [ "id" | "description" | "price"] {
                   ___________________________________
                   "id1" | "nice set"    | 1000
                   "id2" | "another set" | 2000 }

        db.update("delete from PRICES where price > :price", [price: 950]) // delete with params
        PRICES.query().numberOfRows.should == 0
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
