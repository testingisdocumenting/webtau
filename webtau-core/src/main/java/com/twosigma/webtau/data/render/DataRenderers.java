package com.twosigma.webtau.data.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.twosigma.webtau.utils.ServiceUtils;
import com.twosigma.webtau.utils.TraceUtils;

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
        renders.add(new TableDataRenderer());
        renders.add(new ToStringRenderer());

        return renders;
    }
}
