package scenarios.rest.springboot

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def customerPayload = [firstName: "FN", lastName: "LN"]

def customer = createLazyResource("customer") { // lazy resource to be created on the first access
    def id = http.post("/customers", customerPayload) {
        return id
    }

    return new Customer(id: id, url: "/customers/${id}") // definition is below
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