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

import org.testingisdocumenting.webtau.cleanup.DeferredCallsRegistration;
import org.testingisdocumenting.webtau.data.MultiValue;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.data.table.TableDataUnderscore;
import org.testingisdocumenting.webtau.data.table.autogen.TableDataCellValueGenFunctions;
import org.testingisdocumenting.webtau.data.table.header.CompositeKey;
import org.testingisdocumenting.webtau.documentation.CoreDocumentation;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.persona.Persona;
import org.testingisdocumenting.webtau.reporter.*;
import org.testingisdocumenting.webtau.utils.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.data.table.TableDataUnderscore.*;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;
import static org.testingisdocumenting.webtau.utils.FunctionUtils.*;

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

    /**
     * creates a map from var args key value
     * @param firstKey first key
     * @param firstValue first value
     * @param restKv key value pairs
     * @param <K> type of key
     * @return map with preserved order
     */
    public static <K> Map<K, Object> mapOf(K firstKey, Object firstValue, Object... restKv) {
        return CollectionUtils.mapOf(firstKey, firstValue, restKv);
    }

    /**
     * creates a map from original map and var args key value overrides
     * @param original original map
     * @param firstKey first key
     * @param firstValue first value
     * @param restKv key value pairs
     * @param <K> type of key
     * @return map with preserved order
     */
    public static <K> Map<K, Object> mapOf(Map<K, ?> original, K firstKey, Object firstValue, Object... restKv) {
        return CollectionUtils.mapOf(original, firstKey, firstValue, restKv);
    }

    public static ValuePath createActualPath(String path) {
        return new ValuePath(path);
    }

    public static void defer(String label, Runnable code) {
        DeferredCallsRegistration.callAfterATest(label, code);
    }

    public static void defer(Runnable code) {
        defer("", code);
    }

    /**
     * sleep for a provided time. This is a bad practice and must be used as a workaround for
     * some of the hardest weird cases.
     * <p>
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

    /**
     * create persona instance
     * @param id persona id
     * @return new persona instance
     */
    public static Persona persona(String id) {
        return Persona.persona(id);
    }

    /**
     * create persona instance with payload like authId, or config values
     * @param id persona id
     * @param payload persona payload
     * @return new persona instance with payload
     */
    public static Persona persona(String id, Map<String, Object> payload) {
        return Persona.persona(id, payload);
    }

    /**
     * create persona instance with payload like authId, or config values
     * @param id persona id
     * @param firstKey payload first key
     * @param firstValue payload first value
     * @param restKv payload additional vararg values
     * @return new persona instance with payload
     */
    public static Persona persona(String id, String firstKey, Object firstValue, Object... restKv) {
        return Persona.persona(id, firstKey, firstValue, restKv);
    }

    public static Persona getCurrentPersona() {
        return Persona.getCurrentPersona();
    }

    public static void step(String label, Runnable action) {
        step(label, toSupplier(action));
    }

    public static <R> R step(String label, Supplier<Object> action) {
        return step(label, Collections.emptyMap(), action);
    }

    public static void step(String label, Map<String, Object> stepInput, Runnable action) {
        step(label, stepInput, toSupplier(action));
    }

    public static <R> R step(String label, Map<String, Object> stepInput, Supplier<Object> action) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(action(label)),
                () -> tokenizedMessage(none("completed"), action(label)),
                action);

        if (!stepInput.isEmpty()) {
            step.setInput(WebTauStepInputKeyValue.stepInput(stepInput));
        }

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    public static void repeatStep(String label, int numberOfAttempts, Runnable action) {
        repeatStep(label, numberOfAttempts, toFunction(action));
    }

    public static void repeatStep(String label, int numberOfAttempts, Consumer<WebTauStepContext> action) {
        Function<WebTauStepContext, Object> asFunc = toFunction(action);
        repeatStep(label, numberOfAttempts, asFunc);
    }

    public static void repeatStep(String label, int numberOfAttempts, Function<WebTauStepContext, Object> action) {
        WebTauStep step = WebTauStep.createRepeatStep(label, numberOfAttempts, action);
        step.execute(StepReportOptions.REPORT_ALL);
    }

    /**
     * outputs provided message for tracing
     * @param label label to print
     */
    public static void trace(String label) {
        trace(label, Collections.emptyMap());
    }

    /**
     * outputs provided key-values to console and web report
     * @param label label to print
     * @param firstKey first key
     * @param firstValue first value
     * @param restKv key-values as vararg
     */
    public static void trace(String label, String firstKey, Object firstValue, Object... restKv) {
        trace(label, CollectionUtils.mapOf(firstKey, firstValue, restKv));
    }

    /**
     * outputs provided key-values to console and web report
     * @param label label to print
     * @param info key-values as a map
     */
    public static void trace(String label, Map<String, Object> info) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(action(label)),
                () -> tokenizedMessage(action(label)),
                () -> {});
        step.setClassifier(WebTauStepClassifiers.TRACE);

        if (!info.isEmpty()) {
            step.setInput(WebTauStepInputKeyValue.stepInput(info));
        }

        step.execute(StepReportOptions.REPORT_ALL);
    }

    /**
     * outputs warning to console and web report
     * @param label label to print
     */
    public static void warning(String label) {
        warning(label, Collections.emptyMap());
    }

    /**
     * outputs warning with additional provided key-values to console and web report
     * @param label label to print
     * @param firstKey first key
     * @param firstValue first value
     * @param restKv key-values as vararg
     */
    public static void warning(String label, String firstKey, Object firstValue, Object... restKv) {
        warning(label, CollectionUtils.mapOf(firstKey, firstValue, restKv));
    }

    /**
     * outputs warning with additional provided key-values to console and web report
     * @param label label to print
     * @param info key-values as a map
     */
    public static void warning(String label, Map<String, Object> info) {
        MessageToken messageToken = IntegrationTestsMessageBuilder.warning(label);
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(messageToken),
                () -> tokenizedMessage(messageToken),
                () -> {});
        step.setClassifier(WebTauStepClassifiers.WARNING);

        if (!info.isEmpty()) {
            step.setInput(WebTauStepInputKeyValue.stepInput(info));
        }

        step.execute(StepReportOptions.REPORT_ALL);
    }

    public static void fail(String message) {
        throw new AssertionError(message);
    }

    public static void fail() {
        throw new AssertionError();
    }

    /**
     * @deprecated use {@link #mapOf} instead
     */
    @Deprecated
    public static <K> Map<K, Object> aMapOf(K firstKey, Object firstValue, Object... rest) {
        return mapOf(firstKey, firstValue, rest);
    }

    /**
     * @deprecated use {@link #mapOf} instead
     */
    @Deprecated
    public static <K> Map<K, Object> aMapOf(Map<K, ?> original, K firstKey, Object firstValue, Object... restKv) {
        return mapOf(original, firstKey, firstValue, restKv);
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
