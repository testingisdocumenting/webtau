/*
 * Copyright 2020 webtau maintainers
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

def rows = $('[role="rowgroup"]:not(.ag-hidden) .ag-row')
def selectedRows = $('[role="rowgroup"]:not(.ag-hidden) .ag-row-selected')

scenario('open grid') {
    browser.open('/ag-grid-multi-select')
}

scenario('multi select with shift') {
    rows.waitTo beVisible()
    rows.click()
    rows.get(3).shiftClick()

    selectedRows.count.waitTo == 3
    selectedRows.should == ['A1\nB1\nC1', 'A2\nB2\nC2', 'A3\nB3\nC3']
}

scenario('multi select with control or meta') {
    browser.refresh()
    rows.waitTo beVisible()

    rows.click()
    rows.get(3).commandOrControlClick()

    selectedRows.count.waitTo == 2
    selectedRows.should == ['A1\nB1\nC1', 'A3\nB3\nC3']
}