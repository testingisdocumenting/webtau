/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.data;

import org.testingisdocumenting.webtau.pdf.Pdf;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class DataPdf {
    /**
     * Use <code>data.pdf.read(String)</code> to read and parse PDF from a path.
     * <p>
     * Passed path is either relative based on working dir or absolute path. Or it can be a resource class path.
     * @param fileOrResourcePath relative file path, absolute file path or classpath resource path
     * @return pdf instance to use to access parsed data
     */
    public Pdf read(String fileOrResourcePath) {
        DataPath dataPath = DataPath.fromFileOrResourcePath(fileOrResourcePath);
        FileOrResourceBinaryDataProvider binaryDataProvider = new FileOrResourceBinaryDataProvider(
                dataPath);

        return readPdfAsStep(binaryDataProvider, fileOrResourcePath,
                () -> dataPath.isResource() ? "classpath resource" : "file",
                () -> dataPath.isResource() ? fileOrResourcePath : dataPath.getFullFilePath().toString());
    }

    /**
     * Use <code>data.pdf.read(BinaryDataProvider)</code> to read PDF data from an instance that implements <code>BinaryDataProvider</code> (e.g. <code>DataNode</code>)
     * @param binaryDataProvider instance of <code>BinaryDataProvider</code>
     * @return pdf instance to use to access parsed data
     */
    public Pdf read(BinaryDataProvider binaryDataProvider) {
        return readPdfAsStep(binaryDataProvider);
    }

    public Pdf read(String id, byte[] pdfData) {
        return readPdfAsStep(new BinaryDataProvider() {
            @Override
            public byte[] getBinaryContent() {
                return pdfData;
            }

            @Override
            public String binaryDataSource() {
                return id;
            }
        });
    }

    public Pdf read(byte[] pdfData) {
        return readPdfAsStep(new BinaryDataProvider() {
            @Override
            public byte[] getBinaryContent() {
                return pdfData;
            }

            @Override
            public String binaryDataSource() {
                return "binary data";
            }
        });
    }

    private Pdf readPdfAsStep(BinaryDataProvider binaryDataProvider) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("parsing").classifier("pdf").from().url(binaryDataProvider.binaryDataSource()),
                (result) -> tokenizedMessage().action("parsed").classifier("pdf").from().url(binaryDataProvider.binaryDataSource()),
                () -> Pdf.pdf(binaryDataProvider)
        );

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    private Pdf readPdfAsStep(BinaryDataProvider binaryDataProvider,
                              String givenPath,
                              Supplier<String> pathSourceSupplier,
                              Supplier<String> fullPathSupplier) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("parsing").classifier("pdf").from().classifier("file or resource").url(givenPath),
                (result) -> tokenizedMessage().action("parsed").classifier("pdf").from().classifier(pathSourceSupplier.get())
                        .url(fullPathSupplier.get()),
                () -> Pdf.pdf(binaryDataProvider)
        );

        return step.execute(StepReportOptions.REPORT_ALL);
    }
}
