/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

scenario('open browser') {
    browser.open('/finders-and-filters')
}

scenario('by css id') {
    def welcomeMessage = $('#welcome')
    welcomeMessage.should == 'hello'
}

scenario('by css first matched') {
    def menu = $('ul li a')
    menu.should == 'book'
}

scenario('by css all matched') {
    def menu = $('ul li a')
    menu.should == ['book', 'orders', 'help']
}

scenario('by css and filter by number') {
    def ordersMenu = $('ul li a').get(2)
    ordersMenu.should == 'orders'
}

scenario('by css and filter by text') {
    def ordersMenu = $('ul li a').get('orders')
    ordersMenu.should == 'orders'
}

scenario('by css and filter by regexp') {
    def ordersMenu = $('ul li a').get(~/ord/)
    ordersMenu.should == 'orders'
}
