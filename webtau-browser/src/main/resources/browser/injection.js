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
      return filterByTextVaue(webElements, function (textValue) {
        return textValue === text;
      });
    }

    function filterByNearby(webElements, target) {
      var result = [];
      var idx = 0;
      var len = webElements.length;

      for (; idx < len; idx++) {
        if (isNearby(webElements[idx], target, 50)) {
          result.push(webElements[idx]);
        }
      }

      return result;
    }

    function filterByRegexp(webElements, regexpText) {
      var regexp = new RegExp(regexpText);

      return filterByTextVaue(webElements, function (textValue) {
        return regexp.test(textValue);
      });
    }

    function filterByTextVaue(webElements, predicate) {
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

    function isNearby(elA, elB, maxDistance) {
      var a = elA.getBoundingClientRect();
      var b = elB.getBoundingClientRect();

      var cornersA = [
        [a.x, a.y],
        [a.x + a.width, a.y],
        [a.x, a.y + a.height],
        [a.x + a.width, a.y + a.height],
      ];

      var cornersB = [
        [b.x, b.y],
        [b.x + b.width, b.y],
        [b.x, b.y + b.height],
        [b.x + b.width, b.y + b.height],
      ];

      var minDistance = 10000 + maxDistance;
      for (var i = 0; i < cornersA.length; i++)
        for (var j = 0; j < cornersB.length; j++) {
          var cornerA = cornersA[i];
          var cornerB = cornersB[j];
          const distance = Math.sqrt(
            Math.pow(cornerB[0] - cornerA[0], 2) +
              Math.pow(cornerB[1] - cornerA[1], 2)
          );

          if (distance < minDistance) {
            minDistance = distance;
          }
        }

      return minDistance <= maxDistance;
    }

    function findByParentCss(webElement, css) {
      if (!webElement || !webElement.parentElement) {
        return undefined;
      }

      return webElement.parentElement.closest(css);
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
      findByParentCss: findByParentCss,
      flashElements: flashElements,
      filterByNearby: filterByNearby,
    };
  })();
