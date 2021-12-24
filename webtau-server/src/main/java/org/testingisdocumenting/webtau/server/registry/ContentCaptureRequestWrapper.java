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

package org.testingisdocumenting.webtau.server.registry;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

public class ContentCaptureRequestWrapper extends HttpServletRequestWrapper {
    private final HttpServletRequest request;
    private final ByteArrayOutputStream capture;

    private ServletInputStream input;

    public ContentCaptureRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
        this.capture = new ByteArrayOutputStream(1024);
    }

    @Override
    public ServletInputStream getInputStream() {
        if (input != null) {
            return input;
        }

        ServletInputStream originalInputStream;
        try {
            originalInputStream = request.getInputStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        input = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return originalInputStream.isFinished();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                originalInputStream.setReadListener(readListener);
            }

            @Override
            public int read() throws IOException {
                int b = originalInputStream.read();
                if (b != -1) {
                    capture.write(b);
                }

                return b;
            }

            @Override
            public void close() throws IOException {
                originalInputStream.close();
                capture.close();
            }

            @Override
            public boolean isReady() {
                return originalInputStream.isReady();
            }
        };

        return input;
    }

    public byte[] getCaptureAsBytes() throws IOException {
        return capture.toByteArray();
    }

    public void close() {
        if (input == null) {
            return;
        }

        try {
            input.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String getCaptureAsString() {
        try {
            String charsetName = getCharacterEncoding();

            return charsetName != null ?
                    new String(getCaptureAsBytes(), charsetName):
                    new String(getCaptureAsBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
