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

package com.twosigma.webtau.page.path

import com.twosigma.webtau.page.path.filter.ByNumberElementsFilter
import com.twosigma.webtau.page.path.filter.ByTextElementsFilter
import com.twosigma.webtau.page.path.finder.ByCssFinder
import org.junit.Test

class ElementPathTest {
    @Test
    void "should render full path description"() {
        def path = new ElementPath()
        path.addFinder(new ByCssFinder("#cssid"))
        path.addFilter(new ByTextElementsFilter("about"))
        path.addFilter(new ByNumberElementsFilter(2))
        path.addFinder(new ByCssFinder(".child"))

        path.toString().should == 'by css #cssid , element(s) with text about , element number 2 , nested find by css .child'
    }

    @Test
    void "should create a copy to be modified later"() {
        def path = new ElementPath()
        path.addFinder(new ByCssFinder("#cssid"))

        def copy = path.copy()
        copy.addFilter(new ByNumberElementsFilter(2))
        copy.toString().should == "by css #cssid , element number 2"

        def anotherCopy = path.copy()
        anotherCopy.addFilter(new ByNumberElementsFilter(3))
        anotherCopy.toString().should == "by css #cssid , element number 3"
    }
}
