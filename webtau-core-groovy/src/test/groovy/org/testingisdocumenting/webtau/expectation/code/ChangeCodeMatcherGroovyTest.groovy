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

package org.testingisdocumenting.webtau.expectation.code

import org.junit.Test
import org.testingisdocumenting.webtau.data.DbEntity

import static org.testingisdocumenting.webtau.Matchers.*

class ChangeCodeMatcherGroovyTest {
    @Test
    void "change java bean single property"() {
        def dbEntity = new DbEntity()
        dbEntity.setId("id1")
        dbEntity.setDescription("description1")
        dbEntity.setValue(100)

        code {
            // change-single-property
            code {
                updateDbEntity(dbEntity)
            } should change("dbEntity.id", dbEntity::getId)
            // change-single-property
        } should throwException(AssertionError)
    }

    @Test
    void "change java bean some properties"() {
        def dbEntity = new DbEntity()
        dbEntity.setId("id1")
        dbEntity.setDescription("description1")
        dbEntity.setValue(100)

        code {
            // change-full-property
            code {
                buggyOperation(dbEntity)
            } should change("dbEntity", dbEntity)
            // change-full-property
        } should throwException(AssertionError)
    }

    private static void updateDbEntity(DbEntity dbEntity) {
        dbEntity.setValue(140)
        dbEntity.setDescription("description-changed")
    }

    private static void buggyOperation(DbEntity dbEntity) {
    }
}
