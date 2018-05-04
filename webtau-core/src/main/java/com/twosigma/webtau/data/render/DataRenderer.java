package com.twosigma.webtau.data.render;

public interface DataRenderer {
    /**
     * Renders data based on its type.
     * returns string representation of a data in case it can handle it.
     * returns null otherwise
     * @param data data to be rendered
     * @return String representation or null if can't handle
     */
    String render(Object data);
}
