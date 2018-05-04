package com.twosigma.webtau.data.render;

import com.twosigma.webtau.data.table.TableData;
import com.twosigma.webtau.data.table.render.DefaultTableRenderStyle;
import com.twosigma.webtau.data.table.render.TableRenderer;

public class TableDataRenderer implements DataRenderer {
    private static final DefaultTableRenderStyle renderStyle = new DefaultTableRenderStyle();

    @Override
    public String render(final Object data) {
        return (data instanceof TableData)
                ? "\n" + TableRenderer.render((TableData) data, renderStyle)
                : null;
    }
}
