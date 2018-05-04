package com.twosigma.webtau.utils;

import com.twosigma.webtau.data.render.DataRenderers;

import java.io.PrintWriter;
import java.io.StringWriter;

public class TraceUtils {
    private TraceUtils() {
    }

    public static String renderValueAndType(Object v) {
        return DataRenderers.render(v) + " " + renderType(v);
    }

    public static String renderType(Object v) {
        return "<" + (v == null ? "null" : v.getClass().getCanonicalName()) + ">";
    }

    public static String stackTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);

        return stringWriter.toString();
    }
}
