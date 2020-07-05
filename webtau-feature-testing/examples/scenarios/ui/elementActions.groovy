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
import static pages.Pages.payments
import static pages.Pages.payments

scenario('open browser') {
    browser.open('/element-actions')
}

scenario('click') {
    payments.confirmation.click()
}

scenario('validate click') {
    payments.message.waitTo == 'single clicked'
}

scenario('hover') {
    $('.menu').hover()
}

scenario('validate hover') {
    def paymentsMenuItem = $('.dropdown .item').get('Payments')
    paymentsMenuItem.waitTo beVisible()
    paymentsMenuItem.click()

    payments.message.waitTo == 'fetching payments'
}

scenario('send keys') {
    payments.dollarAmount.sendKeys("104.5")
}

scenario('send keys validation') {
    payments.dollarAmount.should == "104.5"
}

scenario('right click') {
    $('table').rightClick()
}

scenario('validate context menu') {
    $('.context-menu-item').waitTo == ['Menu Item A', 'Menu Item B']
}

scenario('double click') {
    $('#expand-area').doubleClick()
}

scenario('validate double click') {
    payments.message.waitTo == 'double clicked'
}