/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.data.render;

class PrettyPrinterCanFitWidthCalculator {
    private final int maxWidth;
    private final int numberOfElements;
    private int currentWidth = 0;
    private int idx = 0;
    private final PrettyPrinter printer;

    PrettyPrinterCanFitWidthCalculator(int maxWidth, int numberOfElements) {
        this.maxWidth = maxWidth;
        this.numberOfElements = numberOfElements;
        this.currentWidth = 0;
        this.idx = 0;
        this.printer = new PrettyPrinter(0);

        addBracketsWidthAdjustment();
    }

    PrettyPrinter getPrinter() {
        return printer;
    }

    void nextIteration() {
        if (idx != numberOfElements - 1) {
            addCommaWidthAdjustment();
        }

        currentWidth += printer.calcMaxWidth();
        idx++;

        printer.clear();
    }

    private void addBracketsWidthAdjustment() {
        currentWidth += 2;
    }

    private void addCommaWidthAdjustment() {
        currentWidth += 2;
    }

    boolean isOutOfBoundaries() {
        return currentWidth > maxWidth || printer.getNumberOfLines() > 1;
    }
}
