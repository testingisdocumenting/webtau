package com.twosigma.webtau.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ResourceUtils {
    private ResourceUtils() {
    }

    /**
     * {@link InputStream} of the specified resource. Throws if resource is not found
     * @param resourcePath resource path like path/to/meta.json
     * @return input stream
     */
    public static InputStream requiredResourceStream(String resourcePath) {
        InputStream stream = ResourceUtils.class.getClassLoader().getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new RuntimeException("can't find resource: " + resourcePath);
        }
        return stream;
    }

    /**
     * {@link InputStream} of the specified resource. Null if resource is not found
     * @param resourcePath resource path like path/to/meta.json
     * @return input stream
     */
    public static InputStream resourceStream(String resourcePath) {
        return ResourceUtils.class.getClassLoader().getResourceAsStream(resourcePath);
    }

    /**
     * textual content from the classpath by resource path
     * @param resourcePath resource path like path/to/meta.json
     * @return text content of the resource
     */
    public static String textContent(String resourcePath) {
        InputStream stream = requiredResourceStream(resourcePath);

        try {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * binary content from the classpath by resource path
     * @param resourcePath resource path like path/to/meta.json
     * @return binary content of the resource
     */
    public static byte[] binaryContent(String resourcePath) {
        InputStream stream = requiredResourceStream(resourcePath);

        try {
            return IOUtils.toByteArray(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * list of text contents from the classpath by resource path. If you have multiple jars/zips and each of them have
     * file-name.txt, this method will give you content of each of the file
     *
     * @param resourcePath resource path like path/to/bundle.txt
     * @return list of text contents
     */
    public static List<String> textContents(String resourcePath) {
        List<String> contents = new ArrayList<>();

        try {
            Enumeration<URL> resources = ResourceUtils.class.getClassLoader().getResources(resourcePath);
            while (resources.hasMoreElements()) {
                contents.add(IOUtils.toString(resources.nextElement(), StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (contents.isEmpty()) {
            throw new IllegalArgumentException("can't find resource: " + resourcePath);
        }

        return contents;
    }
}
