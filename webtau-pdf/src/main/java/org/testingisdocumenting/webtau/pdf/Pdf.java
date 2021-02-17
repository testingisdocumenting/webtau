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

package org.testingisdocumenting.webtau.pdf;

import org.testingisdocumenting.webtau.cleanup.CleanupRegistration;
import org.testingisdocumenting.webtau.data.traceable.CheckLevel;
import org.testingisdocumenting.webtau.http.datanode.DataNode;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Pdf {
    private static final Set<Pdf> openedPdfs = Collections.synchronizedSet(new HashSet<>());
    private final PDDocument document;

    static {
        registerCleanup();
    }

    private Pdf(byte[] content) throws IOException {
        document = PDDocument.load(content);
        openedPdfs.add(this);
    }

    public static Pdf pdf(byte[] content) {
        try {
            return new Pdf(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Pdf pdf(DataNode node) {
        if (!node.isBinary()) {
            throw new AssertionError("response is not binary content");
        }

        try {
            Pdf pdf = Pdf.pdf((byte[]) node.get());
            node.getTraceableValue().updateCheckLevel(CheckLevel.FuzzyPassed);

            return pdf;
        } catch (Exception e) {
            node.getTraceableValue().updateCheckLevel(CheckLevel.ExplicitFailed);
            throw new AssertionError("Failed to parse pdf: " + e.getMessage());
        }
    }

    public static synchronized void closeAll() {
        openedPdfs.forEach(Pdf::closeWithoutRemove);
        openedPdfs.clear();
    }

    public PdfText pageText(int pageIdx) {
        try {
            PDFTextStripper reader = new PDFTextStripper();
            reader.setStartPage(pageIdx + 1);
            reader.setEndPage(pageIdx + 1);

            return new PdfText("body.pdf.pageIdx(" + pageIdx + ").text", reader.getText(document));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        closeWithoutRemove();
        openedPdfs.remove(this);
    }

    private static void registerCleanup() {
        CleanupRegistration.registerForCleanup("closing", "close", "pdfs",
                () -> !openedPdfs.isEmpty(),
                Pdf::closeAll);
    }

    private void closeWithoutRemove() {
        try {
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
