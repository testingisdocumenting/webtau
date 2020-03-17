package scenarios.rest

import static org.testingisdocumenting.webtau.WebTauDsl.http
import static org.testingisdocumenting.webtau.WebTauGroovyDsl.scenario

scenario("extracting id after POST to use inside GET request") {
    def id = http.post("/employee", [firstName: 'FN', lastName: 'LN']) {
        return id
    }
    http.doc.capture('employee-post')

    http.get("/employee/$id") {
        firstName.should == 'FN'
        lastName.should == 'LN'
    }
    http.doc.capture('employee-get') // capture previous HTTP call into <docDir>/employee-get
}
