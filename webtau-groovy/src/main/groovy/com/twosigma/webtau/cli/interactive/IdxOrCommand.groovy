/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.cli.interactive

import com.twosigma.webtau.utils.NumberUtils
import groovy.transform.PackageScope

@PackageScope
class IdxOrCommand {
    Integer idx
    InteractiveCommand command

    IdxOrCommand(String text) {
        command = findCommand(text)

        if (! command) {
            idx = convertToNumber(text)
        }
    }

    private static InteractiveCommand findCommand(String text) {
        return InteractiveCommand.values().find { it.matches(text) }
    }

    private static Integer convertToNumber(String text) {
        try {
            return NumberUtils.convertStringToNumber(text)
        } catch (ParseException) {
            return null
        }
    }
}
