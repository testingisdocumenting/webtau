/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau;

import org.testingisdocumenting.webtau.data.MultiValue;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.data.table.TableDataUnderscore;
import org.testingisdocumenting.webtau.data.table.autogen.TableDataCellValueGenFunctions;
import org.testingisdocumenting.webtau.data.table.header.CompositeKey;
import org.testingisdocumenting.webtau.documentation.CoreDocumentation;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.persona.Persona;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.CollectionUtils;

import java.util.Arrays;
import java.util.Map;

import static org.testingisdocumenting.webtau.data.table.TableDataUnderscore.UNDERSCORE;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

/**
 * Convenient class for a single static * imports to have matchers and helper functions available for your test
 */
public class WebTauCore extends Matchers {
    public static final CoreDocumentation doc = new CoreDocumentation();

    public static final TableDataCellValueGenFunctions cell = new TableDataCellValueGenFunctions();

    public static TableData table(String... columnNames) {
        return new TableData(Arrays.stream(columnNames));
    }

    public static TableData table(Object... columnNames) {
        return new TableData(Arrays.stream(columnNames));
    }

    public static CompositeKey key(Object... values) {
        return new CompositeKey(Arrays.stream(values));
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

    /**
     * sleep for a provided time. This is a bad practice and must be used as a workaround for
     * some of the hardest weird cases.
     *
     * consider using waitTo approach on various layers: wait for UI to change, wait for HTTP resource to be updated,
     * wait for file to change, DB result to be different, etc.
     * @param millis number of milliseconds to wait
     */
    public static void sleep(long millis) {
        WebTauStep.createAndExecuteStep(
                tokenizedMessage(action("sleeping"), FOR, numberValue(millis), classifier("milliseconds")),
                () -> tokenizedMessage(action("slept"), FOR, numberValue(millis), classifier("milliseconds")),
                () -> {
                    try {
                        Thread.sleep(millis);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public static Persona persona(String id) {
        return Persona.persona(id);
    }

    public static Persona persona(String id, Map<String, Object> payload) {
        return Persona.persona(id, payload);
    }

    public static Persona getCurrentPersona() {
        return Persona.getCurrentPersona();
    }

    public static final TableDataUnderscore __ = UNDERSCORE;
    public static final TableDataUnderscore ___ = UNDERSCORE;
    public static final TableDataUnderscore ____ = UNDERSCORE;
    public static final TableDataUnderscore _____ = UNDERSCORE;
    public static final TableDataUnderscore ______ = UNDERSCORE;
    public static final TableDataUnderscore _______ = UNDERSCORE;
    public static final TableDataUnderscore ________ = UNDERSCORE;
    public static final TableDataUnderscore _________ = UNDERSCORE;
    public static final TableDataUnderscore __________ = UNDERSCORE;
    public static final TableDataUnderscore ___________ = UNDERSCORE;
    public static final TableDataUnderscore ____________ = UNDERSCORE;
    public static final TableDataUnderscore _____________ = UNDERSCORE;
    public static final TableDataUnderscore ______________ = UNDERSCORE;
    public static final TableDataUnderscore _______________ = UNDERSCORE;
    public static final TableDataUnderscore ________________ = UNDERSCORE;
    public static final TableDataUnderscore _________________ = UNDERSCORE;
    public static final TableDataUnderscore __________________ = UNDERSCORE;
    public static final TableDataUnderscore ___________________ = UNDERSCORE;
    public static final TableDataUnderscore ____________________ = UNDERSCORE;
    public static final TableDataUnderscore _____________________ = UNDERSCORE;
    public static final TableDataUnderscore ______________________ = UNDERSCORE;
    public static final TableDataUnderscore _______________________ = UNDERSCORE;
    public static final TableDataUnderscore ________________________ = UNDERSCORE;
    public static final TableDataUnderscore _________________________ = UNDERSCORE;
    public static final TableDataUnderscore __________________________ = UNDERSCORE;
    public static final TableDataUnderscore ___________________________ = UNDERSCORE;
    public static final TableDataUnderscore ____________________________ = UNDERSCORE;
    public static final TableDataUnderscore _____________________________ = UNDERSCORE;
    public static final TableDataUnderscore ______________________________ = UNDERSCORE;
    public static final TableDataUnderscore _______________________________ = UNDERSCORE;
    public static final TableDataUnderscore ________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _________________________________ = UNDERSCORE;
    public static final TableDataUnderscore __________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ___________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ____________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _____________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ______________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _______________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore __________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ___________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ____________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _____________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ______________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _______________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore __________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ___________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ____________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _____________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ______________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _______________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore __________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ___________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ____________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _____________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ______________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _______________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore __________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ___________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ____________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _____________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ______________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _______________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore __________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ___________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ____________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _____________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ______________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _______________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore __________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ___________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ____________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _____________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ______________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _______________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore __________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ___________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ____________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _____________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ______________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _______________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore __________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ___________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ____________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _____________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ______________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _______________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ________________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _________________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore __________________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ___________________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ____________________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _____________________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ______________________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore _______________________________________________________________________________________________________________ = UNDERSCORE;
    public static final TableDataUnderscore ________________________________________________________________________________________________________________ = UNDERSCORE;
}
