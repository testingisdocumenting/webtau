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

package org.testingisdocumenting.webtau.expectation.code;

import org.junit.Test;
import org.testingisdocumenting.webtau.data.DbEntity;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*;

public class ChangeCodeMatcherJavaTest {
    @Test
    public void changeJavaBeanSingleProperty() {
        var dbEntity = new DbEntity();
        dbEntity.setId("id1");
        dbEntity.setDescription("description1");
        dbEntity.setValue(100);

        code(() -> {
            updateDbEntity(dbEntity);
        }).should(change("dbEntity.value", dbEntity::getValue));
    }

    @Test
    public void failToChangeJavaBeanSingleProperty() {
        var dbEntity = new DbEntity();
        dbEntity.setId("id1");
        dbEntity.setDescription("description1");
        dbEntity.setValue(100);

        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "javabean-id-change-fail-output", """
                > expecting code to change value of dbEntity.id
                X failed expecting code to change value of dbEntity.id:
                      actual:     "id1" <java.lang.String>
                    expected: not "id1" <java.lang.String> (Xms)""", () -> {
            // change-single-property
            code(() -> {
                updateDbEntity(dbEntity);
            }).should(change("dbEntity.id", dbEntity::getId));
            // change-single-property
        });
    }

    @Test
    public void changeJavaBeanProperties() {
        var dbEntity = new DbEntity();
        dbEntity.setId("id1");
        dbEntity.setDescription("description1");
        dbEntity.setValue(100);

        code(() -> {
            updateDbEntity(dbEntity);
        }).should(change("dbEntity", dbEntity));
    }

    @Test
    public void failToChangeJavaBeanProperties() {
        var dbEntity = new DbEntity();
        dbEntity.setId("id1");
        dbEntity.setDescription("description1");
        dbEntity.setValue(100);

        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "javabean-full-change-fail-output", """
                > expecting code to change value of dbEntity
                X failed expecting code to change value of dbEntity:
                    dbEntity.description:  actual:     "description1" <java.lang.String>
                                         expected: not "description1" <java.lang.String>
                    dbEntity.id:  actual:     "id1" <java.lang.String>
                                expected: not "id1" <java.lang.String>
                    dbEntity.value:  actual: 100 <java.lang.Integer>
                                   expected: not 100 <java.lang.Integer> (Xms)""", () -> {
            // change-full-property
            code(() -> {
                buggyOperation(dbEntity);
            }).should(change("dbEntity", dbEntity));
            // change-full-property
        });
    }

    private void updateDbEntity(DbEntity dbEntity) {
        dbEntity.setValue(140);
        dbEntity.setDescription("description-changed");
    }

    private void buggyOperation(DbEntity dbEntity) {
    }
}