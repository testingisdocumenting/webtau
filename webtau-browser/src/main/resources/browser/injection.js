/*
 * Copyright 2020 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

window._webtau =
  window._webtau ||
  (function () {
    function elementsDetails(webElements) {
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
          outerHtml: webElement.outerHTML,
          innerText: webElement.innerText,
          value: webElement.value,
          attributes: extractAttributes(webElement),
        };
      }

      function extractAttributes(webElement) {
        var result = {};

        var attrs = webElement.attributes;
        var idx = 0;
        var len = attrs.length;

        for (; idx < len; idx++) {
          result[attrs[idx].name] = attrs[idx].value;
        }

        return result;
      }
    }

    function filterByText(webElements, text) {
      return filterBy(webElements, function (textValue) {
        return textValue === text;
      });
    }

    function filterByRegexp(webElements, regexpText) {
      var regexp = new RegExp(regexpText);

      return filterBy(webElements, function (textValue) {
        return regexp.test(textValue);
      });
    }

    function filterBy(webElements, predicate) {
      var metas = elementsDetails(webElements);
      var result = [];
      var idx = 0;
      var len = metas.length;

      for (; idx < len; idx++) {
        var meta = metas[idx];
        if (predicate(textValueToUse(meta))) {
          result.push(webElements[idx]);
        }
      }

      return result;
    }

    function textValueToUse(meta) {
      if (meta.tagName === "input" || meta.tagName === "textarea") {
        return meta.value;
      }

      return trimmedInnerText();

      function trimmedInnerText() {
        return meta.innerText ? meta.innerText.trim() : meta.innerText;
      }
    }

    function flashElements(webElements) {
      var svg = createSvg(webElements);

      document.body.appendChild(svg);

      flashElement(svg, 3, 200, function () {
        document.body.removeChild(svg);
      });

      function createSvg(elements) {
        var svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
        svg.style.position = "fixed";
        svg.style.top = "0";
        svg.style.right = "0";
        svg.style.bottom = "0";
        svg.style.left = "0";
        svg.zIndex = 9999;

        svg.setAttribute("width", window.innerWidth);
        svg.setAttribute("height", window.innerHeight);
        svg.setAttribute(
          "viewport",
          "0 0 " + window.innerWidth + " " + window.innerHeight
        );

        var len = Math.min(elements.length, 100);
        for (var idx = 0; idx < len; idx++) {
          svg.appendChild(createSvgRect(elements[idx]));
        }

        return svg;
      }

      function createSvgRect(element) {
        var rect = element.getBoundingClientRect();

        var overlay = document.createElementNS(
          "http://www.w3.org/2000/svg",
          "rect"
        );
        overlay.setAttribute("x", rect.x);
        overlay.setAttribute("y", rect.y);
        overlay.setAttribute("width", rect.width);
        overlay.setAttribute("height", rect.height);
        overlay.setAttribute("fill", "red");
        overlay.style.fill = "#2c91ea";

        return overlay;
      }

      function flashElement(el, numberOfTimes, delay, onComplete) {
        flashOnce(0);

        function flashOnce(initialShowDelay) {
          setTimeout(function () {
            el.style.display = "block";
            setTimeout(function () {
              el.style.display = "none";
              numberOfTimes--;

              if (numberOfTimes > 0) {
                flashOnce(delay);
              } else {
                onComplete();
              }
            }, delay);
          }, initialShowDelay);
        }
      }
    }

    return {
      elementsDetails: elementsDetails,
      filterByText: filterByText,
      filterByRegexp: filterByRegexp,
      flashElements: flashElements,
    };
  })();
