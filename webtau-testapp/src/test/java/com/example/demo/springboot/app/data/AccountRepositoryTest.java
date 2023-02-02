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
import static org.testingisdocumenting.webtau.db.Database.db;

// repository-test-config
@DataJpaTest
@ActiveProfiles("tc") // test profile with Test Containers
public class AccountRepositoryTest {
    private final AccountRepository accountRepository;
    private final Database mainDb;
    private final DatabaseTable ACCOUNT;

    @Autowired
    public AccountRepositoryTest(DataSource dataSource, AccountRepository accountRepository) {
        this.accountRepository = accountRepository;

        mainDb = db.labeled("main-db").fromDataSource(dataSource); // define WebTau database instance to insert/read
        ACCOUNT = mainDb.table("ACCOUNT"); // define ACCOUNT table instance to insert/read
    }
    // repository-test-config

    @BeforeEach
    public void cleanupBeforeTest() {
        mainDb.update("delete from ACCOUNT");
    }

    @Test
    public void findById() {
        // define data to be inserted into DB
        TableData newAccounts = table( "ID", "FIRST_NAME", "LAST_NAME",
                                      ________________________________,
                                      "id1", "FN1"       , "LN1",
                                      "id2", "FN2"       , "LN2");

        // insert directly to DB bypassing repository
        ACCOUNT.insert(newAccounts);

        // use accounts repository to fetch a record
        Account account = accountRepository.findById("id2").get();

        // use bean and map comparison shortcut
        actual(account).should(equal(map(
                "id", "id2",
                "firstName", "FN2",
                "lastName", "LN2")));
    }

    @Test
    public void findByIdReuseData() {
        // using camelCase for properties
        TableData newAccounts = table( "*id", "firstName" , "lastName",
                                      ________________________________,
                                       "id1", "FN1"       , "LN1",
                                       "id2", "FN2"       , "LN2");

        // WebTau will automatically convert camelCase to underscores at insert time
        ACCOUNT.insert(newAccounts);

        // use accounts repository to fetch a record
        Account account = accountRepository.findById("id2").get();

        // reuse row from newAccounts table as expected value
        actual(account).should(equal(newAccounts.findByKey("id2")));
    }

    @Test
    public void createEntriesAndFindByName() {
        TableData newAccounts = table( "*id", "firstName" , "lastName",
                                      ________________________________,
                                       "id1", "FN1"       , "LN",
                                       "id2", "FN2"       , "LN",
                                       "id3", "FNN1"      , "LNN");

        List<Account> accounts = createAccounts(newAccounts); // create accounts java beans from table data
        accountRepository.saveAll(accounts);

        // force data commit to DB
        TestTransaction.flagForCommit();
        TestTransaction.end();

        // query all data from DB, using * to note that we depend on ID for compare and not order
        ACCOUNT.query().should(equal(table("*ID", "FIRST_NAME", "LAST_NAME",
                                          ________________________________,
                                           "id1", "FN1"       , "LN",
                                           "id2", "FN2"       , "LN",
                                           "id3", "FNN1"      , "LNN")));

        // WebTau will automatically convert actual column names from underscores to camelCase based on expected column names
        ACCOUNT.query().should(equal(newAccounts));

        // search by last name and validate received java beans
        List<Account> lnAccounts = accountRepository.findByLastName("LN");
        actual(lnAccounts).should(equal(table("*id", "firstName", "lastName",
                                              ______________________________,
                                              "id1", "FN1"       , "LN",
                                              "id2", "FN2"       , "LN")));
    }

    @Test
    public void createEntriesAndFindByNameReuseData() {
        TableData newAccounts = table("*id", "firstName", "lastName",
                                      ______________________________,
                                      "id1", "FN1"      , "LN",
                                      "id2", "FN2"      , "LN",
                                      "id3", "FNN1"     , "LNN");

        List<Account> accounts = createAccounts(newAccounts); // create accounts java beans from table data
        accountRepository.saveAll(accounts);

        // force data commit to DB
        TestTransaction.flagForCommit();
        TestTransaction.end();

        // another way to compare.
        // WebTau will automatically convert actual column names from underscores to camelCase based on expected column names format
        ACCOUNT.query().should(equal(newAccounts));

        // search by last name and validate received java beans
        List<Account> lnAccounts = accountRepository.findByLastName("LN");
        actual(lnAccounts).should(equal(table("*id", "firstName", "lastName",
                                              ______________________________,
                                              "id1", "FN1"       , "LN",
                                              "id2", "FN2"       , "LN")));
    }

    private static List<Account> createAccounts(TableData tableData) {
        return tableData.rowsStream().map(row -> {
            Account account = new Account();
            account.setId(row.get("id"));
            account.setFirstName(row.get("firstName"));
            account.setLastName(row.get("lastName"));

            return account;
        }).collect(Collectors.toList());
    }
}
