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

import pages.Pages

import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario("""# Alternative Search
to do different description
""") {
    Pages.search.open()
    Pages.search.box.should == "search this1"
}

scenario("""# Alternative Mega Search""") {
    Pages.search.open()
}
