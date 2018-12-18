package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario('open forms') {
    browser.open('/special-forms')
}

scenario('get set custom based on registered handler') {
    def customFormElement = $('#answer')

    customFormElement.setValue('hello')
    customFormElement.should == 'hello'
}