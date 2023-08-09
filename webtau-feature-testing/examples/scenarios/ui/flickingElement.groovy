/*
 * Copyright 2022 webtau maintainers
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

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def flickingText = $('#flicking-text')

scenario('text') {
    browser.reopen('/flicking-element')
    flickingText.waitTo == 'done'
}

scenario('visible') {
    browser.reopen('/flicking-element')

    10.times {
        flickingText.waitToBe visible
    }
}

scenario('count') {
    browser.reopen('/flicking-element-collection')

    def collection = $(".entry").get(~/e5/)
    10.times {
        collection.count.waitToBe > 0
    }
}