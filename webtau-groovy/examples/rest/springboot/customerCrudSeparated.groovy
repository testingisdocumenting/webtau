package rest.springboot

import static com.twosigma.webtau.WebTauGroovyDsl.*

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
    customer.id.shouldNot == null // accessing resource for the first time will trigger POST (in this example)
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