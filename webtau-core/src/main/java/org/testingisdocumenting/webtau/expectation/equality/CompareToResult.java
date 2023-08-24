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

package org.testingisdocumenting.webtau.expectation.equality;

import java.util.ArrayList;
import java.util.List;

public class CompareToResult {
    private List<ValuePathMessage> equalMessages = new ArrayList<>();
    private List<ValuePathMessage> notEqualMessages = new ArrayList<>();
    private List<ValuePathMessage> greaterMessages = new ArrayList<>();
    private List<ValuePathMessage> lessMessages = new ArrayList<>();
    private List<ValuePathMessage> missingMessages = new ArrayList<>();
    private List<ValuePathMessage> extraMessages = new ArrayList<>();

    public boolean isEqual() {
        return notEqualMessages.isEmpty() && hasNoExtraAndNoMissing();
    }

    public boolean isNotEqual() {
        return equalMessages.isEmpty() || hasExtraOrMissing();
    }

    public boolean isGreater() {
        return lessMessages.isEmpty() && equalMessages.isEmpty() && hasNoExtraAndNoMissing();
    }

    public boolean isGreaterOrEqual() {
        return lessMessages.isEmpty() && hasNoExtraAndNoMissing();
    }

    public boolean isLess() {
        return greaterMessages.isEmpty() && equalMessages.isEmpty() && hasNoExtraAndNoMissing();
    }

    public boolean isLessOrEqual() {
        return greaterMessages.isEmpty() && hasNoExtraAndNoMissing();
    }

    public List<ValuePathMessage> getEqualMessages() {
        return equalMessages;
    }

    void setEqualMessages(List<ValuePathMessage> equalMessages) {
        this.equalMessages = equalMessages;
    }

    public List<ValuePathMessage> getNotEqualMessages() {
        return notEqualMessages;
    }

    void setNotEqualMessages(List<ValuePathMessage> notEqualMessages) {
        this.notEqualMessages = notEqualMessages;
    }

    public List<ValuePathMessage> getGreaterMessages() {
        return greaterMessages;
    }

    void setGreaterMessages(List<ValuePathMessage> greaterMessages) {
        this.greaterMessages = greaterMessages;
    }

    public List<ValuePathMessage> getLessMessages() {
        return lessMessages;
    }

    void setLessMessages(List<ValuePathMessage> lessMessages) {
        this.lessMessages = lessMessages;
    }

    public List<ValuePathMessage> getMissingMessages() {
        return missingMessages;
    }

    public boolean hasNoExtraAndNoMissing() {
        return extraMessages.isEmpty() && missingMessages.isEmpty();
    }

    public boolean hasExtraOrMissing() {
        return !extraMessages.isEmpty() || !missingMessages.isEmpty();
    }

    void setMissingMessages(List<ValuePathMessage> missingMessages) {
        this.missingMessages = missingMessages;
    }

    public List<ValuePathMessage> getExtraMessages() {
        return extraMessages;
    }

    void setExtraMessages(List<ValuePathMessage> extraMessages) {
        this.extraMessages = extraMessages;
    }
}
