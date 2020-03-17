package com.twosigma.webtau.http.testserver;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseUtils {
    public static Map<String, String> echoHeaders(HttpServletRequest request) {
        Map<String, String> header = new LinkedHashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            header.put(name, request.getHeader(name));
        }
        return header;
    }

    public static byte[] echoBody(HttpServletRequest request) {
        try {
            return IOUtils.toByteArray(request.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
