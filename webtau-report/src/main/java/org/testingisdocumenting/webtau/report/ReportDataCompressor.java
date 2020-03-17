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

package org.testingisdocumenting.webtau.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

public class ReportDataCompressor {
    public static String compressAndBase64(String reportData) {
        try {
            return compressAndBase64Impl(reportData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String compressAndBase64Impl(String jsonData) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = new GZIPOutputStream(outputStream);
        gzipOut.write(jsonData.getBytes());
        gzipOut.close();

        return encodeBase64(outputStream.toByteArray());
    }

    private static String encodeBase64(byte[] content) {
        return Base64.getEncoder().encodeToString(content);
    }
}
