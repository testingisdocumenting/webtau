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

        PRICES.createQuery().should == ["ID" | "DESCRIPTION" | "PRICE"] {
                                 ___________________________________
                                 "id1" | "nice set"    | 1000
                                 "id2" | "another set" | 2000 }

        db.createQuery("select * from PRICES where id='id2'").should ==
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

        PRICES.query().numberOfRows().should == 2
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

        doc.capture('db-setup-permute-table', PRICES.query())
        PRICES.query().numberOfRows().should == 6
    }

    @Test
    void "use csv read data"() {
        db.update("delete from PRICES")

        def PRICES = db.table("PRICES")
        PRICES << data.csv.table('prices-db.csv')

        doc.capture('db-setup-csv-table', PRICES.query())
        PRICES.should == [ "*ID" | "DESCRIPTION"  | "AVAILABLE" | "TYPE" | "PRICE"] {
                          __________________________________________________________
                           "id1" | "description1" |        true | "card" | 200
                           "id2" | "description2" |       false |  "rts" | 400 }
    }

    @Test
    void "query method on table should be optional during comparison"() {
        setupPrices()

        // query whole table start
        def PRICES = db.table("PRICES")
        PRICES.should == ["ID" | "DESCRIPTION" | "PRICE"] {
                         ___________________________________
                         "id1" | "nice set"    | 1000
                         "id2" | "another set" | 2000 }
        // query whole table end
    }

    @Test
    void "query table with select statement"() {
        setupPrices()

        // query without where clause start
        def prices = db.createQuery("select * from PRICES")
        prices.should == ["ID" | "DESCRIPTION" | "PRICE"] {
                         ___________________________________
                         "id1" | "nice set"    | 1000
                         "id2" | "another set" | 2000 }
        // query without where clause end
    }

    @Test
    void "query table with select statement and param"() {
        setupPrices()
        // query with where clause start
        def prices = db.createQuery("select * from PRICES where id=:id", [id: "id1"])
        prices.should == ["ID" | "DESCRIPTION" | "PRICE"] {
                         ___________________________________
                         "id1" | "nice set"    | 1000     }
        // query with where clause end
    }

    @Test
    void "create query is lazy"() {
        setupPrices()
        // query with where clause start
        def prices = db.createQuery("select * from PRICES")
        db.update("delete from PRICES")
        prices.should == []
        // query with where clause end
    }

    @Test
    void "query table to match one row and assert against map"() {
        setupPrices()
        // query with where clause start
        def prices = db.createQuery("select * from PRICES where id=:id", [id: "id1"])
        prices.should == [ID: "id1", "DESCRIPTION": "nice set", PRICE: 1000]
        // query with where clause end
    }

    @Test
    void "query table to match one row using single param shortcut and assert against map"() {
        setupPrices()
        // query with where clause start
        def prices = db.createQuery("select * from PRICES where id=:id or external_id=:id", "id1")
        prices.should == [ID: "id1", "DESCRIPTION": "nice set", PRICE: 1000]
        // query with where clause end
    }

    @Test
    void "query table to match multiple row and assert against map"() {
        setupPrices()
        def prices = db.createQuery("select * from PRICES")

        code {
            prices.should == [ID: "id1", "DESCRIPTION": "nice set", PRICE: 1000]
        } should throwException(~/(?s).*TableData.*Map.*/)
    }

    @Test
    void "query table with select statement and array param"() {
        setupPrices()
        // query with where clause start
        def prices = db.createQuery("select * from PRICES where id in (:ids)", [ids: ["id1", "id2"]])
        prices.should == ["ID" | "DESCRIPTION" | "PRICE"] {
                         ___________________________________
                         "id1" | "nice set"    | 1000
                         "id2" | "another set" | 2000     }
        // query with where clause end
    }

    @Test
    void "wait for count to change"() {
        setupPrices()
        def insertRowThread = Thread.start {
            sleep 1000
            db.table('PRICES') << ["ID" | "DESCRIPTION" | "PRICE"] {
                                  ___________________________________
                                  "id3" | "cheese set"  | 3000 }
        }

        // query with where clause start
        def count = db.createQuery("select count(*) from PRICES")
        count.should == 2

        // event happen somewhere to increase the number of rows...
        count.waitTo == 3
        // query with where clause end

        insertRowThread.join()
    }

    @Test
    void "should query single value"() {
        setupPrices()

        def price = db.createQuery("select price from PRICES where id='id1'")
        price.should == 1000
        price.shouldNot == 2000
    }

    @Test
    void "should query single value with params"() {
        setupPrices()

        // query single value params start
        def price = db.createQuery("select price from PRICES where id=:id", [id: 'id1'])
        price.should == 1000
        price.shouldNot == 2000
        // query single value params end
    }

    @Test
    void "value returned from query is a special wrapper value"() {
        setupPrices()
        // single value access start
        def price = db.createQuery("select price from PRICES where id=:id", [id: 'id1'])
        if (price.querySingleValue() > 100) {
            println("do something")
        }
        // single value access end
    }

    @Test
    void "delete with params"() {
        setupPrices()

        db.update("delete from PRICES where price > :price", [price: 950])
        db.table("PRICES").query().numberOfRows().should == 0
    }

    @Test
    void "should run updates with params"() {
        def PRICES = setupPrices()
        doc.capture('db-before-update', PRICES.query())

        db.update("update PRICES set price=:price where id=:id", [id: 'id2', price: 4000])
        doc.capture('db-after-update', PRICES.query())

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

        PRICES.query().numberOfRows().should == 0
    }
}
