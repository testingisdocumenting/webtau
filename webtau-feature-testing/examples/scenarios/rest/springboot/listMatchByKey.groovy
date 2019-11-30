package scenarios.rest.springboot

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("list Customers and assert with a Table Data using key column") {
    def id1 = createCustomer firstName: "FN1", lastName: "LN1"
    def id2 = createCustomer firstName: "FN2", lastName: "LN2"
    def id3 = createCustomer firstName: "FN3", lastName: "LN3"

    http.get("/customers") {
        content.should == ['*id' | 'firstName' | 'lastName'] {
                           _________________________________
                             id2 |       'FN2' |      'LN2'
                             id1 |       'FN1' |      'LN1'
                             id3 |       'FN3' |      'LN3'}
    }
}

def createCustomer(Map payload) {
    def id = http.post("/customers", payload) { id }
    http.doc.capture('create-customer')

    return id
}