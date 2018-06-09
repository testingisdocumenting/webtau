/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.expectation.equality;

import java.util.ArrayList;
import java.util.List;

public class CompareToResult {
    private List<ActualPathMessage> equalMessages = new ArrayList<>();
    private List<ActualPathMessage> notEqualMessages = new ArrayList<>();
    private List<ActualPathMessage> greaterMessages = new ArrayList<>();
    private List<ActualPathMessage> lessMessages = new ArrayList<>();
    private List<ActualPathMessage> missingMessages = new ArrayList<>();
    private List<ActualPathMessage> extraMessages = new ArrayList<>();

    public List<ActualPathMessage> getEqualMessages() {
        return equalMessages;
    }

    void setEqualMessages(List<ActualPathMessage> equalMessages) {
        this.equalMessages = equalMessages;
    }

    public List<ActualPathMessage> getNotEqualMessages() {
        return notEqualMessages;
    }

    void setNotEqualMessages(List<ActualPathMessage> notEqualMessages) {
        this.notEqualMessages = notEqualMessages;
    }

    public List<ActualPathMessage> getGreaterMessages() {
        return greaterMessages;
    }

    void setGreaterMessages(List<ActualPathMessage> greaterMessages) {
        this.greaterMessages = greaterMessages;
    }

    public List<ActualPathMessage> getLessMessages() {
        return lessMessages;
    }

    void setLessMessages(List<ActualPathMessage> lessMessages) {
        this.lessMessages = lessMessages;
    }

    public List<ActualPathMessage> getMissingMessages() {
        return missingMessages;
    }

    public boolean hasNoExtraOrMissing() {
        return extraMessages.isEmpty() && missingMessages.isEmpty();
    }

    void setMissingMessages(List<ActualPathMessage> missingMessages) {
        this.missingMessages = missingMessages;
    }

    public List<ActualPathMessage> getExtraMessages() {
        return extraMessages;
    }

    void setExtraMessages(List<ActualPathMessage> extraMessages) {
        this.extraMessages = extraMessages;
    }
}
