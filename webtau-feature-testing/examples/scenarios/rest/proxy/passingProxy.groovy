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

package scenarios.rest.proxy

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("using proxy") {
    step('check http proxy') {
        System.getProperty('http.proxyHost').should == '1.2.3.4'
        System.getProperty('http.proxyPort').should == '1234'
    }

    step('check https proxy') {
        System.getProperty('https.proxyHost').should == '5.6.7.8'
        System.getProperty('https.proxyPort').should == '5678'
    }

    step('check no proxy') {
        System.getProperty('http.nonProxyHosts').should == '*.host1|host2.*'
    }
}