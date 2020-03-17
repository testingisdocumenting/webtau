/*
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
     * {@link URL} of the specified resource. Null if resource is not found
     * @param resourcePath resource path like path/to/meta.json
     * @return url of the resource
     */
    public static URL resourceUrl(String resourcePath) {
        return ResourceUtils.class.getClassLoader().getResource(resourcePath);
    }

    /**
     * checks if given resource exists
     * @param resourcePath resource path
     * @return true if resource exists
     */
    public static boolean hasResource(String resourcePath) {
        return resourceStream(resourcePath) != null;
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
