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

package org.testingisdocumenting.webtau.report;

import org.testingisdocumenting.webtau.utils.JsonUtils;
import org.testingisdocumenting.webtau.utils.ResourceUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class ReactJsBundle {
    private final String javaScript;
    private final String css;

    public ReactJsBundle() {
        Manifest manifest = Manifest.load();
        this.javaScript = manifest.combineJavaScripts();
        this.css = manifest.combineCss();
    }

    public String getJavaScript() {
        return javaScript;
    }

    public String getCss() {
        return css;
    }

    public static class Manifest {
        private Map<String, String> files;
        private List<String> entrypoints;

        public Map<String, String> getFiles() {
            return files;
        }

        public void setFiles(Map<String, String> files) {
            this.files = files;
        }

        public List<String> getEntrypoints() {
            return entrypoints;
        }

        public void setEntrypoints(List<String> entrypoints) {
            this.entrypoints = entrypoints;
        }

        public static Manifest load() {
            String assetManifest = ResourceUtils.textContent("asset-manifest.json");
            return JsonUtils.deserializeAs(assetManifest, Manifest.class);
        }

        public String combineJavaScripts() {
            return combineBasedOnFilter(p -> p.endsWith(".js"));
        }

        public String combineCss() {
            return combineBasedOnFilter(p -> p.endsWith(".css"));
        }

        private String combineBasedOnFilter(Predicate<String> predicate) {
            return entrypoints.stream()
                    .filter(predicate)
                    .map(ResourceUtils::textContent)
                    .collect(Collectors.joining(""));
        }
    }
}
