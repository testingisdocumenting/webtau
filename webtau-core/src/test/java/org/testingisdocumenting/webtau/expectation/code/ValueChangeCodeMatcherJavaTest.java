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

public class ValueChangeCodeMatcherJavaTest {
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
    public void shouldNotChangeJavaBeanSingleProperty() {
        var dbEntity = new DbEntity();
        dbEntity.setId("id1");
        dbEntity.setDescription("description1");
        dbEntity.setValue(100);

        code(() -> {
            changeFreeOperation(dbEntity);
        }).shouldNot(change("dbEntity.value", dbEntity::getValue));
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
    public void failToNotChangeJavaBeanSingleProperty() {
        var dbEntity = new DbEntity();
        dbEntity.setId("id1");
        dbEntity.setDescription("description1");
        dbEntity.setValue(100);

        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "javabean-value-not-change-fail-output", """
                > expecting code to not change value of dbEntity.value
                X failed expecting code to not change value of dbEntity.value:
                      actual: 140 <java.lang.Integer>
                    expected: 100 <java.lang.Integer> (Xms)""", () -> {
            // change-not-single-property
            code(() -> {
                changeFreeBuggyOperation(dbEntity);
            }).shouldNot(change("dbEntity.value", dbEntity::getValue));
            // change-not-single-property
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

    @Test
    public void failNotToChangeJavaBeanProperties() {
        var dbEntity = new DbEntity();
        dbEntity.setId("id1");
        dbEntity.setDescription("description1");
        dbEntity.setValue(100);

        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "javabean-fail-not-to-change-output", """
                > expecting code to not change value of dbEntity
                X failed expecting code to not change value of dbEntity:
                    dbEntity.description:  actual: "description-changed" <java.lang.String>
                                         expected: "description1" <java.lang.String>
                                                               ^
                    dbEntity.value:  actual: 110 <java.lang.Integer>
                                   expected: 100 <java.lang.Integer> (Xms)""", () -> {
            // change-not-full-property
            code(() -> {
                calculatePrice(dbEntity);
            }).shouldNot(change("dbEntity", dbEntity));
            // change-not-full-property
        });
    }

    private void updateDbEntity(DbEntity dbEntity) {
        dbEntity.setValue(140);
        dbEntity.setDescription("description-changed");
    }

    private void calculatePrice(DbEntity dbEntity) {
        dbEntity.setValue(110);
        dbEntity.setDescription("description-changed");
    }

    private void buggyOperation(DbEntity dbEntity) {
    }

    private void changeFreeOperation(DbEntity dbEntity) {
    }

    private void changeFreeBuggyOperation(DbEntity dbEntity) {
        dbEntity.setValue(140);
        dbEntity.setDescription("description-changed");
    }
}