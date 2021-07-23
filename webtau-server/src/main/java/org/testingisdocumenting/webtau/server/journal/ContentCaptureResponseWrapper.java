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

package org.testingisdocumenting.webtau.server.journal;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

class ContentCaptureResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream capture;
    private final HttpServletResponse response;
    private ServletOutputStream output;

    public ContentCaptureResponseWrapper(HttpServletResponse response) {
        super(response);
        this.response = response;
        capture = new ByteArrayOutputStream(response.getBufferSize());
    }

    @Override
    public ServletOutputStream getOutputStream() {
        if (output == null) {
            ServletOutputStream originalOutputStream;
            try {
                originalOutputStream = response.getOutputStream();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            output = new ServletOutputStream() {
                @Override
                public void write(int b) throws IOException {
                    originalOutputStream.write(b);
                    capture.write(b);
                }

                @Override
                public void flush() throws IOException {
                    originalOutputStream.flush();
                    capture.flush();
                }

                @Override
                public void close() throws IOException {
                    originalOutputStream.close();
                    capture.close();
                }

                @Override
                public boolean isReady() {
                    return originalOutputStream.isReady();
                }

                @Override
                public void setWriteListener(WriteListener listener) {
                    originalOutputStream.setWriteListener(listener);
                }
            };
        }

        return output;
    }

    @Override
    public PrintWriter getWriter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flushBuffer() throws IOException {
        super.flushBuffer();

        if (output != null) {
            output.flush();
        }
    }

    public byte[] getCaptureAsBytes() throws IOException {
        return capture.toByteArray();
    }

    public void close() {
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    public String getCaptureAsString() throws IOException {
        return new String(getCaptureAsBytes(), getCharacterEncoding());
    }
}
