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

window._webtau = window._webtau || {
    elementsMeta: function(webElements) {
        var idx = 0;
        var len = webElements.length;
        var result = [];

        for (; idx < len; idx++) {
            result.push(extractMeta(webElements[idx]));
        }

        return result;

        function extractMeta(webElement) {
            return {
                tagName: webElement.tagName.toLowerCase(),
                innerHtml: webElement.innerHTML,
                value: webElement.value,
                attributes: extractAttributes(webElement)
            }
        }

        function extractAttributes(webElement) {
            var result = {};

            var attrs = webElement.attributes;
            var idx = 0;
            var len = attrs.length;

            for(; idx < len; idx++) {
                result[attrs[idx].name] = attrs[idx].value;
            }

            return result;
        }
    },

    flashElements: function(webElements) {
        var overlays = createOverlays();

        attachOverlays();

        flash(overlays, 3, 200, function() {
            detachOverlays();
        });

        function createOverlays() {
            var result = [];
            var len = webElements.length;
            for (var idx = 0; idx < len; idx++) {
                result.push(createOverlayFor(webElements[idx]));
            }

            return result;
        }

        function attachOverlays() {
            var len = overlays.length;
            for (var idx = 0; idx < len; idx++) {
                document.body.appendChild(overlays[idx]);

            }
        }

        function detachOverlays() {
            var len = overlays.length;
            for (var idx = 0; idx < len; idx++) {
                document.body.removeChild(overlays[idx]);
            }
        }

        function createOverlayFor(webElement) {
            var rect = webElement.getBoundingClientRect();

            var overlay = document.createElement('div');
            overlay.id = 'webtau-overlay';
            overlay.style.position = 'absolute';
            overlay.style.top = rect.top;
            overlay.style.right = rect.right;
            overlay.style.bottom = rect.bottom;
            overlay.style.left = rect.left;
            overlay.style.width = rect.width;
            overlay.style.height = rect.height;
            overlay.style.backgroundColor = '#146fd4';
            overlay.style.opacity = 0.5;
            overlay.zIndex = 9000;

            return overlay;
        }

        function show(elements) {
            var len = elements.length;
            for (var idx = 0; idx < len; idx++) {
                elements[idx].style.display = 'block';
            }
        }

        function hide(elements) {
            var len = elements.length;
            for (var idx = 0; idx < len; idx++) {
                elements[idx].style.display = 'none';
            }
        }

        function flash(elements, numberOfTimes, delay, onComplete) {
            flashOnce(0)

            function flashOnce(initialShowDelay) {
                setTimeout(function() {
                    show(elements);
                    setTimeout(function() {
                        hide(elements);
                        numberOfTimes--;

                        if (numberOfTimes > 0) {
                            flashOnce(delay);
                        } else {
                            onComplete();
                        }
                    }, delay)
                }, initialShowDelay);
            }
        }
    }
};