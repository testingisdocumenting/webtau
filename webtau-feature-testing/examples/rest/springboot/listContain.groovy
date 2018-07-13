package rest.springboot

import static com.twosigma.webtau.Ddjt.contain
import static com.twosigma.webtau.WebTauDsl.http
import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario("list Customers and assert that it contains a specified entry") {
    http.get("/customers/search/findAllByOrderByLastName") {
        _embedded.customers.should contain([firstName: 'FN1', lastName: 'LN1'])
    }
}