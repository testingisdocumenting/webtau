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

package com.twosigma.webtau.documentation.components;

public class Game {
    private String title;
    private int basePrice;
    private int discountPercent;

    public Game(String title, int basePrice, int discountPercent) {
        this.title = title;
        this.basePrice = basePrice;
        this.discountPercent = discountPercent;
    }

    public String getTitle() {
        return title;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public int getDiscountedPrice() {
        return basePrice * discountPercent / 100;
    }

    @Override
    public String toString() {
        return title + " (" + getDiscountedPrice() + " crystals)";
    }
}
