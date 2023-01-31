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

package org.testingisdocumenting.webtau.db

import org.junit.Test

import static DatabaseDsl.*

class DatabaseTestContainersTest {
    @Test
    void "from test containers jdbc"() {
        def postgresqlDb = db.labeled("my-db").fromJdbc("jdbc:tc:postgresql:9.6.8:///my-db", "test", "test", "")

        postgresqlDb.update("""
CREATE TABLE IF NOT EXISTS TEST_TABLE (
    id varchar(255),
    available bool
)
"""
        )

        postgresqlDb.table("TEST_TABLE") << [id: "test-id", available: true]

        def count = postgresqlDb.query("select count(*) from TEST_TABLE").singleValue()
        count.should == 1
    }
}
