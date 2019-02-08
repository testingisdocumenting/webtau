/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario('open') {
    search.submit('query')

    browser.open("/search") // page is not be ing refreshed
    search.searchMessage.should == 'searching for query'
}

scenario('reopen') {
    search.submit('name')

    browser.reopen("/search") // page is going to be refreshed
    search.searchMessage.should == ''
}

scenario('refresh') {
    search.submit('name')
    browser.refresh() // page is going to be refreshed

    search.searchMessage.should == ''
}

scenario('restart') {
    browser.open('/local-storage')
    browser.localStorage.setItem('favoriteColor', 'pretty')

    browser.refresh()
    $('#favorite-color').should == 'pretty'

    browser.restart()
    $('#favorite-color').should == ''
}

scenario('save url') {
    browser.open('/resource-creation')

    $('#resource-id').should == '' // :remove from docs:
    $('#new').click()
    browser.saveCurrentUrl()
}

scenario('change url in between save and load') {
    search.submit('query')
}

scenario('load url') {
    browser.openSavedUrl()
    $('#resource-id').should != '' // :remove from docs:
    // continue resource related manipulations
}

scenario('wait on url') {
    browser.open('/resource-creation')

    $('#new').click()
    browser.url.ref.waitTo == 'created-id'
}