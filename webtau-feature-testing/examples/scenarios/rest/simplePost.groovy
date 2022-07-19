package scenarios.rest

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("extracting id after POST to use inside GET request") {
    def id = http.post("/employee", [firstName: 'FN', lastName: 'LN']) {
        id.shouldNot == ""
        return id
    }
    http.doc.capture('employee-post') // capture previous HTTP call into <docDir>/employee-post

    http.get("/employee/$id") {
        firstName.should == 'FN'
        lastName.should == 'LN'
    }
    http.doc.capture('employee-get') // capture previous HTTP call into <docDir>/employee-get
}
