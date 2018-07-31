package scenarios.rest.springboot

import static com.twosigma.webtau.WebTauDsl.http
import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario("list Customers and assert with a Table Data") {
    http.post("/customers", [firstName: "FN1", lastName: "LN1"])
    http.post("/customers", [firstName: "FN2", lastName: "LN2"])
    http.post("/customers", [firstName: "FN3", lastName: "LN3"])

    http.get("/customers/search/findAllByOrderByLastName") {
        _embedded.customers.should == ['firstName' | 'lastName'] {
                                       __________________________
                                             'FN1' |      'LN1'
                                             'FN2' |      'LN2'
                                             'FN3' |      'LN3' }
    }

    http.doc.capture('list-match')
}