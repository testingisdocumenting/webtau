/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau;

import com.twosigma.webtau.data.MultiValue;
import com.twosigma.webtau.data.table.TableData;
import com.twosigma.webtau.data.table.TableDataUnderscore;
import com.twosigma.webtau.data.table.autogen.TableDataCellAbove;
import com.twosigma.webtau.data.table.autogen.TableDataCellValueGenFunctions;
import com.twosigma.webtau.data.table.autogen.TableDataCellValueGenerator;
import com.twosigma.webtau.documentation.CoreDocumentation;
import com.twosigma.webtau.expectation.ActualCode;
import com.twosigma.webtau.expectation.ActualCodeExpectations;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.ActualValue;
import com.twosigma.webtau.expectation.ActualValueExpectations;
import com.twosigma.webtau.expectation.CodeBlock;
import com.twosigma.webtau.expectation.code.ThrowExceptionMatcher;
import com.twosigma.webtau.expectation.contain.ContainMatcher;
import com.twosigma.webtau.expectation.equality.EqualMatcher;
import com.twosigma.webtau.expectation.equality.GreaterThanMatcher;
import com.twosigma.webtau.expectation.equality.GreaterThanOrEqualMatcher;
import com.twosigma.webtau.expectation.equality.LessThanMatcher;
import com.twosigma.webtau.expectation.equality.LessThanOrEqualMatcher;
import com.twosigma.webtau.expectation.equality.NotEqualMatcher;
import com.twosigma.webtau.utils.CollectionUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Convenient class for a single static * imports to have matchers and helper functions available for your test
 */
public class WebTauCore extends Matchers {
    public static final CoreDocumentation doc = new CoreDocumentation();

    public static final TableDataCellValueGenFunctions cell = new TableDataCellValueGenFunctions();

    public static final TableDataUnderscore __ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ____ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ____________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ____________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ____________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ____________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ____________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ________________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ____________________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ________________________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ____________________________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ________________________________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ____________________________________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ________________________________________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ____________________________________________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ________________________________________________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ____________________________________________________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ________________________________________________________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ____________________________________________________________________________________________ = TableDataUnderscore.INSTANCE;
    public static final TableDataUnderscore ________________________________________________________________________________________________ = TableDataUnderscore.INSTANCE;

    public static TableData table(String... columnNames) {
        return new TableData(Arrays.stream(columnNames));
    }

    public static TableData table(Object... columnNames) {
        return new TableData(Arrays.stream(columnNames));
    }

    public static MultiValue permute(Object atLeastOneValue, Object... values) {
        return new MultiValue(atLeastOneValue, values);
    }

    public static <K, V> Map<K, V> aMapOf(Object... kvs) {
        return CollectionUtils.aMapOf(kvs);
    }

    public static ActualPath createActualPath(String path) {
        return new ActualPath(path);
    }
}
