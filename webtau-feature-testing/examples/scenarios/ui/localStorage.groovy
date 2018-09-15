package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("local storage api") {
    browser.open('/local-storage')
    browser.localStorage.setItem('favoriteColor', 'clean')

    def color = $('#favorite-color')

    browser.reopen('/local-storage')
    color.should == 'clean'

    browser.localStorage.clear()
    browser.reopen('/local-storage')
    color.should == ''
}