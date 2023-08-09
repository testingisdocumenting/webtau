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

package org.testingisdocumenting.webtau.utils;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

record RecordOne(String id, String name) {
}

public class JavaRecordUtilsTest {
    @Test
    public void isRecord() {
        assertTrue(JavaRecordUtils.isRecord(new RecordOne("id1", "name1")));
        assertFalse(JavaRecordUtils.isRecord(new LinkedHashMap<>()));
    }

    @Test
    public void convertToMap() {
        Map<String, ?> actual = JavaRecordUtils.convertRecordToMap(new RecordOne("id1", "name1"));

        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put("id", "id1");
        expected.put("name", "name1");

        assertEquals(expected, actual);
    }
}