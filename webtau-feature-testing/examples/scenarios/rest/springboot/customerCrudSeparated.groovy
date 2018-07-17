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

package scenarios.rest.springboot

import static com.twosigma.webtau.WebTauDsl.http
import static com.twosigma.webtau.WebTauGroovyDsl.createLazyResource
import static com.twosigma.webtau.WebTauGroovyDsl.scenario

class Customer {
    Number id
    String url // store url of the created entity
}

def customerPayload = [firstName: "FN", lastName: "LN"]

def customer = createLazyResource("customer") { // lazy resource to be created on the first access
    int id = http.post("/customers", customerPayload) {
        return id
    }

    return new Customer(id: id, url: "/customers/${id}")
}

scenario("customer create") {
    customer.id.should != null // accessing resource for the first time will trigger POST (in this example)
}

scenario("customer read") {
    http.get(customer.url) { // convenient re-use of url defined above
        body.should == customerPayload
    }
}

scenario("customer update") {
    def changedLastName = "NLN"
    http.put(customer.url, [*:customerPayload, lastName: changedLastName]) {
        lastName.should == changedLastName
    }

    http.get(customer.url) {
        lastName.should == changedLastName
    }
}

scenario("customer delete") {
    http.delete(customer.url) {
        statusCode.should == 204
    }

    http.get(customer.url) {
        statusCode.should == 404
    }
}