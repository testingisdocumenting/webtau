/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.data;

import org.testingisdocumenting.webtau.utils.StringUtils;

import java.text.NumberFormat;

class DataCsvAutoNumberConversion implements DataCsvValueConverter {
    private final NumberFormat numberFormat;

    DataCsvAutoNumberConversion() {
        numberFormat = NumberFormat.getNumberInstance();
    }

    @Override
    public Object convert(String columnName, String value) {
        return StringUtils.isNumeric(numberFormat, value) ?
                StringUtils.convertToNumber(numberFormat, value):
                value;
    }
}
