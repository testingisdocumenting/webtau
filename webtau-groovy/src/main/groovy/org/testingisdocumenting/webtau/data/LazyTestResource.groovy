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

package org.testingisdocumenting.webtau.data

import java.util.function.Supplier

class LazyTestResource<E> implements GroovyInterceptable {
    private E suppliedValue
    private Supplier<E> resourceValueSupplier
    private String resourceName

    LazyTestResource(String resourceName, Supplier<E> resourceValueSupplier) {
        this.resourceName = resourceName
        this.resourceValueSupplier = resourceValueSupplier
    }

    @Override
    Object getProperty(String propertyName) {
        // we don't extract into a separate method to avoid recursion
        if (this.@suppliedValue == null) {
            this.@suppliedValue = this.@resourceValueSupplier.get()
        }

        return this.@suppliedValue."$propertyName"
    }

    @Override
    def invokeMethod(String name, Object args) {
        // we don't extract into a separate method to avoid recursion
        if (this.@suppliedValue == null) {
            this.@suppliedValue = this.@resourceValueSupplier.get()
        }

        if (name == 'get') {
            return this.@suppliedValue
        }

        return this.@suppliedValue.invokeMethod(name, args)
    }

    @Override
    String toString() {
        // we don't extract into a separate method to avoid recursion
        if (this.@suppliedValue == null) {
            this.@suppliedValue = this.@resourceValueSupplier.get()
        }

        return this.@suppliedValue.toString()
    }
}
