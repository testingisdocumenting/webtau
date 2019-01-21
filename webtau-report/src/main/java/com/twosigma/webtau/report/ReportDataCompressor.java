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

package com.twosigma.webtau.report;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

public class ReportDataCompressor {
    public static String compressAndBase64(String reportData) {
        try {
            return compressAndBase64Impl(reportData);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String compressAndBase64Impl(String jsonData) throws UnsupportedEncodingException {
        LZ4Factory factory = LZ4Factory.nativeInstance();

        byte[] data = jsonData.getBytes("UTF-8");
        final int decompressedLength = data.length;

        LZ4Compressor compressor = factory.highCompressor();
        int maxCompressedLength = compressor.maxCompressedLength(decompressedLength);
        byte[] compressed = new byte[maxCompressedLength];
        int compressedLength = compressor.compress(data, 0, decompressedLength, compressed, 0, maxCompressedLength);

        byte[] compressedSliced = Arrays.copyOfRange(compressed, 0, compressedLength);
        return encodeBase64(compressedSliced);
    }

    private static String encodeBase64(byte[] content) {
        return Base64.getEncoder().encodeToString(content);
    }
}
