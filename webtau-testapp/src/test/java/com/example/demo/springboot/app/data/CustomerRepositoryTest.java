/*
 * Copyright 2023 webtau maintainers
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

package com.example.demo.springboot.app.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.db.Database;
import org.testingisdocumenting.webtau.db.DatabaseTable;

import javax.sql.DataSource;

import java.util.List;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.db.DatabaseDsl.db;

// repository-test-config
@DataJpaTest
@ActiveProfiles("tc") // test profile with Test Containers
public class CustomerRepositoryTest {
    private final CustomerRepository customerRepository;
    private final Database mainDb;
    private final DatabaseTable CUSTOMER;

    @Autowired
    public CustomerRepositoryTest(CustomerRepository customerRepository, DataSource dataSource) {
        this.customerRepository = customerRepository;

        mainDb = db.labeled("main-db").fromDataSource(dataSource); // define WebTau database instance to insert/read
        CUSTOMER = mainDb.table("CUSTOMER"); // define CUSTOMERS table instance to insert/read
    }
    // repository-test-config

    @BeforeEach
    public void cleanupBeforeTest() {
        mainDb.update("delete from CUSTOMER");
    }

    @Test
    public void findById() {
        // define data to be inserted into DB
        TableData newCustomers = table("ID", "FIRST_NAME", "LAST_NAME",
                                      ________________________________,
                                         1L, "FN1"       , "LN1",
                                         2L, "FN2"       , "LN2");

        // insert directly to DB bypassing repository
        CUSTOMER.insert(newCustomers);

        // use our repository to fetch a record
        Customer customer = customerRepository.findById(2L).get();

        // use bean and map comparison shortcut
        actual(customer).should(equal(map(
                "id", 2L,
                "firstName", "FN2",
                "lastName", "LN2")));
    }

    @Test
    public void createEntriesAndFindByName() {
        TableData newCustomers = table("id", "firstName", "lastName",
                                       ________________________________,
                                         1L, "FN1"       , "LN",
                                         2L, "FN2"       , "LN",
                                         3L, "FNN1"      , "LNN");

        List<Customer> customers = createCustomers(newCustomers); // create customers java beans from table data
        customerRepository.saveAll(customers);

        // force data commit to DB
        TestTransaction.flagForCommit();
        TestTransaction.end();

        // query all data from DB, using * to note that we depend on ID for compare and not order
        CUSTOMER.query().should(equal(table("*ID", "FIRST_NAME", "LAST_NAME",
                                           ________________________________,
                                               1L, "FN1"       , "LN",
                                               2L, "FN2"       , "LN",
                                               3L, "FNN1"      , "LNN")));

        // search by last name and validate received java beans
        List<Customer> lnCustomers = customerRepository.findByLastName("LN");
        actual(lnCustomers).should(equal(table("*id", "firstName", "lastName",
                                                ________________________________,
                                                 1L, "FN1"       , "LN",
                                                 2L, "FN2"       , "LN")));
    }

    private static List<Customer> createCustomers(TableData tableData) {
        return tableData.rowsStream().map(row -> {
            Customer customer = new Customer();
            customer.setId(row.get("id"));
            customer.setFirstName(row.get("firstName"));
            customer.setLastName(row.get("lastName"));

            return customer;
        }).collect(Collectors.toList());
    }
}
