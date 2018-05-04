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

package com.twosigma.webtau.data

import java.util.function.Supplier

class LazyTestResource<E> {
    private E originalCache
    private Supplier<E> originalSupplier
    private String resourceName

    /**
     * Multiple scenarios may need the same data setup. If you want those scenarios to run independently,
     * data needs to be initialized on the first request. Typically this is done by moving
     * initialization to `beforeAll` sort of function. In webtau you create lazy resources instead.
     *
     * <pre>
     * def lazySharedData = createLazyResource("resource name") {
     *      def result = callToInitializeTheResouce()
     *      return new MySharedData(firstName: result.firstName, score: result.score)
     * }
     * ...
     * scenario('scenario description') {
     *     http.get("/resource/${lazySharedData.firstName}") {
     *         ...
     *     }
     * }
     * </pre>
     * @param name name of the resource
     * @param supplier resource initialization function
     */
    static <E> E createLazyResource(String name, Supplier<E> supplier) {
        return new LazyTestResource<E>(name, supplier)
    }

    private LazyTestResource(String resourceName, Supplier<E> originalSupplier) {
        this.resourceName = resourceName
        this.originalSupplier = originalSupplier
    }

    def getProperty(String name) {
        synchronized (this) {
            if (this.@originalCache == null) {
                this.@originalCache = this.@originalSupplier.get()
            }

            return this.@originalCache."$name"
        }
    }
}
