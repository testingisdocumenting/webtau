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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

public class SmallBean {
    private int multiplier = 1;

    public SmallBean() {
    }

    public SmallBean(int multiplier) {
        this.multiplier = multiplier;
    }

    public long getPrice() {
        return 100L * multiplier;
    }

    public void setPrice(long price) {
    }

    public String getName() {
        return "n" + multiplier;
    }

    public void setName(String name) {
    }

    public String getDescription() {
        return "d" + multiplier;
    }

    public void setDescription(String description) {
    }
}
