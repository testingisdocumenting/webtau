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

scenario("drag and drop on element") {
    browser.reopen("/drag-and-drop-jquery")

    // drag-and-drop-over
    def draggable = $("#draggable")
    def dropZone = $("#drop-zone")

    draggable.dragAndDropOver(dropZone)
    // drag-and-drop-over

    $("#drop-zone").waitTo == "Dropped"
}

scenario("drag and drop by offset") {
    browser.reopen("/drag-and-drop-jquery")

    // drag-and-drop-by
    def draggable = $("#draggable")
    draggable.dragAndDropBy(50, 50)
    // drag-and-drop-by

    $("#drop-zone").waitTo == "Dropped"
}
