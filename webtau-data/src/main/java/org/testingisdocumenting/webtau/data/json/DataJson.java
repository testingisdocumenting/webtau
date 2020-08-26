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

package org.testingisdocumenting.webtau.data.json;

import org.testingisdocumenting.webtau.data.DataContentUtils;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.util.List;
import java.util.Map;

public class DataJson {
    public Map<String, ?> map(String fileOrResourcePath) {
        return JsonUtils.deserializeAsMap(DataContentUtils.dataTextContent(fileOrResourcePath));
    }

    public List<?> list(String fileOrResourcePath) {
        return JsonUtils.deserializeAsList(DataContentUtils.dataTextContent(fileOrResourcePath));
    }

    public Object object(String fileOrResourcePath) {
        return JsonUtils.deserialize(DataContentUtils.dataTextContent(fileOrResourcePath));
    }
}
