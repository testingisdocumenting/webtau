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

package com.twosigma.webtau.data.render;

import com.twosigma.webtau.utils.ServiceUtils;
import com.twosigma.webtau.utils.TraceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataRenderers {
    private static final List<DataRenderer> renders = discover();

    public static String render(Object data) {
        return renders.stream().
            map(r -> r.render(data)).
            filter(Objects::nonNull).
            findFirst().orElseThrow(() -> new IllegalStateException(
                "No render found for: " + TraceUtils.renderValueAndType(data)));
    }

    private static List<DataRenderer> discover() {
        List<DataRenderer> renders = new ArrayList<>();
        renders.add(new NullRenderer());
        renders.addAll(ServiceUtils.discover(DataRenderer.class));
        renders.add(new ByteArrayRenderer());
        renders.add(new TableDataRenderer());
        renders.add(new StringRenderer());
        renders.add(Objects::toString);

        return renders;
    }
}
