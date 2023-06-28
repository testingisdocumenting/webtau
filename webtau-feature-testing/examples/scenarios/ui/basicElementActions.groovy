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

scenario("open browser") {
    browser.open("/element-actions")
}

scenario("click") {
    payments.confirmation.click()
}

scenario("validate click") {
    payments.message.waitTo == "single clicked"
}

scenario("shift click") {
    payments.confirmation.shiftClick()
}

scenario("validate shift click") {
    payments.message.waitTo == "single clicked with shift"
}

scenario("alt click") {
    payments.confirmation.altClick()
}

scenario("validate alt click") {
    payments.message.waitTo == "single clicked with alt"
}

scenario("control or command click") {
    payments.confirmation.commandOrControlClick()
}

scenario("validate control or command click") {
    payments.message.waitTo == ~/single clicked with command|control/
}

scenario("control click") {
    payments.confirmation.controlClick()
}

scenario("command click") {
    payments.confirmation.commandClick()
}

def menu = $(".menu")
scenario("hover") {
    menu.hover()
}

scenario("validate hover") {
    def paymentsMenuItem = $(".dropdown .item").get("Payments")
    paymentsMenuItem.waitToBe visible
    paymentsMenuItem.click()

    payments.message.waitTo == "fetching payments"
}

scenario("send keys") {
    payments.dollarAmount.sendKeys("104.5")
}

scenario("send keys no log") {
    payments.secret.sendKeysNoLog("my secret token")
}

scenario("send keys validation") {
    payments.dollarAmount.should == "104.5"
}

scenario("clear") {
    payments.dollarAmount.clear()
}

scenario("clear validation") {
    payments.dollarAmount.should == ""
}

scenario("setValue") {
    payments.dollarAmount.setValue("104.5")
}

scenario("setValue noLog") {
    payments.secret.setValueNoLog("my secret token")
}

scenario("setValue validation") {
    payments.dollarAmount.should == "104.5"
}

def paymentsTable = $("table")
scenario("right click") {
    paymentsTable.rightClick()
}

scenario("validate context menu") {
    $(".context-menu-item").waitTo == ["Menu Item A", "Menu Item B"]
}

def expandArea = $("#expand-area")
scenario("double click") {
    expandArea.doubleClick()
}

scenario("validate double click") {
    payments.message.waitTo == "double clicked"
}