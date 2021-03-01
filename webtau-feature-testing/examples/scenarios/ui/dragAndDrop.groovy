/*
 * Copyright 2021 webtau maintainers
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

scenario("open browser") {
    browser.open("/drag-and-drop-jquery")
}

scenario("drag and drop on element") {
    // drag-and-drop
    def draggable = $("#draggable")
    def dropZone = $("#drop-zone")

    draggable.dragAndDropOver(dropZone)
    // drag-and-drop

    $("#drop-zone").waitTo == "Dropped"
}
