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

import org.junit.AfterClass
import org.junit.Test

import static org.testingisdocumenting.webtau.db.DatabaseFacade.db

class ConfigBasedDbProviderTest {
    @AfterClass
    static void cleanup() {
        DbConfig.reset()
    }

    @Test
    void "should use data source provider for primary database"() {
        DbConfig.setDbPrimaryUrl("jdbc:h2:mem:dbconfig;DB_CLOSE_DELAY=-1")
        DbConfig.setDbPrimaryUserName("sa")
        db.query("select 1")
    }
}
