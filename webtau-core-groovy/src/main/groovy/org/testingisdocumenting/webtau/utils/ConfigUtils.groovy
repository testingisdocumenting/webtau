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

package org.testingisdocumenting.webtau.utils

class ConfigUtils {
    static <E> List<E> instancesFromClassList(List classesList) {
        def classes = (List<Class<E>>) classesList
        if (!classes) {
            return []
        }

        return classes.collect { c -> (E) constructFromClass(c) }
    }

    private static Object constructFromClass(Class handlerClass) {
        def defaultConstructor = handlerClass.constructors.find { constructor -> constructor.parameterCount == 0 }
        if (!defaultConstructor) {
            throw new IllegalArgumentException("${handlerClass} must have default constructor")
        }
        return defaultConstructor.newInstance()
    }
}
