/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.http.validation;

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.http.datanode.DataNode;
import org.testingisdocumenting.webtau.http.datanode.DataNodeBuilder;
import org.testingisdocumenting.webtau.http.datanode.DataNodeId;
import org.testingisdocumenting.webtau.http.request.HttpApplicationMime;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.reporter.WebTauStepInput;
import org.testingisdocumenting.webtau.utils.JsonParseException;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.util.Collections;
import java.util.Map;

public class HttpStepInput implements WebTauStepInput {
    private final HttpValidationResult validationResult;

    public HttpStepInput(HttpValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        if (validationResult.getRequestBody() == null) {
            return;
        }

        if (validationResult.getRequestBody().isEmpty()) {
            printer.printLine(Color.YELLOW, "[no request body]");
        } else if (validationResult.getRequestBody().isBinary()) {
            printer.printLine(Color.YELLOW, "[binary request]");
        } else {
            printer.printLine(Color.YELLOW, "request", Color.CYAN, " (", validationResult.getRequestBody().type(), "):");
            renderRequestBody(printer, validationResult.getRequestBody());
        }
    }

    @Override
    public Map<String, ?> toMap() {
        return Collections.emptyMap();
    }

    private void renderRequestBody(PrettyPrinter printer, HttpRequestBody requestBody) {
        if (requestBody.type().equals(HttpApplicationMime.JSON)) {
            try {
                DataNode dataNode = DataNodeBuilder.fromValue(new DataNodeId("request"),
                        JsonUtils.deserialize(requestBody.asString()));

                printer.printObject(dataNode);
                printer.flushCurrentLine();
            } catch (JsonParseException e) {
                printer.printLine(Color.RED, "can't parse request:");
                printer.printLine(requestBody.asString());
                printer.printLine(Color.RED, e.getMessage());
            }
        } else {
            printer.printLine(requestBody.asString());
        }
    }
}
