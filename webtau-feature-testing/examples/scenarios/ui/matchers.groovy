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
    browser.open('/matchers')
}

scenario('equal text') {
    def message = $('#message')
    message.should == 'Select option'
}

scenario('equal text regexp') {
    def message = $('#message')
    message.should == ~/option/
}

scenario('equal list of text') {
    def menu = $('#menu ul li')
    menu.should == ['Hello', 'Text', 'World']
}

scenario('equal list of text and regexp') {
    def menu = $('#menu ul li')
    menu.should == ['Hello', ~/T..t/, 'World']
}

scenario('equal number') {
    def total = $('#total')
    total.should == 300.6
}

scenario('equal list of numbers') {
    def split = $('#split ul li')
    split.should == [100, 28, 172.6]
}

scenario('greater number') {
    def total = $('#total')
    total.shouldBe > 200
}

scenario('greater equal number') {
    def total = $('#total')
    total.shouldBe >= 300
}

scenario('less equal list mix of numbers') {
    def split = $('#split ul li')
    split.should == [100, lessThan(100), greaterThanOrEqual(150)]
}
