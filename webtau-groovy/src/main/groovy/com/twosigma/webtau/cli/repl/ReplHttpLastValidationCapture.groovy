/*
 * Copyright 2020 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.cli.repl

import com.twosigma.webtau.http.datanode.GroovyDataNode
import com.twosigma.webtau.http.validation.HttpValidationHandler
import com.twosigma.webtau.http.validation.HttpValidationResult

class ReplHttpLastValidationCapture implements HttpValidationHandler {
    static GroovyDataNode header
    static GroovyDataNode body

    @Override
    void validate(HttpValidationResult validationResult) {
        header = new GroovyDataNode(validationResult.headerNode)
        body = new GroovyDataNode(validationResult.bodyNode)
    }
}
