package rest

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("extracting id after POST to use inside GET request") {
    def id = http.post("/employee", [firstName: 'FN', lastName: 'LN']) {
        return id
    }
    http.doc.capture('employee-post')

    http.get("/employee/$id") {
        firstName.should == 'FN'
        lastName.should == 'LN'
    }
    http.doc.capture('employee-get') // capture previous HTTP call as <docDir>/employee-get
}