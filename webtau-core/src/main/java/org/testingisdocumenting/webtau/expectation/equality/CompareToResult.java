/*
 * Copyright 2023 webtau maintainers
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

public class CompareToResult {
    // lists are null by default to avoid unnecessary lists instantiation
    private ValuePathLazyMessageList equalMessages;
    private ValuePathLazyMessageList notEqualMessages;
    private ValuePathLazyMessageList greaterMessages;
    private ValuePathLazyMessageList lessMessages;
    private ValuePathLazyMessageList missingMessages;
    private ValuePathLazyMessageList extraMessages;

    public int numberOfMismatches() {
        return size(notEqualMessages) +
                size(greaterMessages) + size(lessMessages) +
                size(missingMessages) + size(extraMessages);
    }

    public boolean isEqual() {
        return isEmpty(notEqualMessages) && hasNoExtraAndNoMissing();
    }

    public boolean isNotEqual() {
        return !isEmpty(notEqualMessages) || hasExtraOrMissing();
    }

    public boolean isGreater() {
        return isEmpty(lessMessages) && isEmpty(equalMessages) && hasNoExtraAndNoMissing();
    }

    public boolean isGreaterOrEqual() {
        return isEmpty(lessMessages) && hasNoExtraAndNoMissing();
    }

    public boolean isLess() {
        return isEmpty(greaterMessages) && isEmpty(equalMessages) && hasNoExtraAndNoMissing();
    }

    public boolean isLessOrEqual() {
        return isEmpty(greaterMessages) && hasNoExtraAndNoMissing();
    }

    public void clear() {
        equalMessages = null;
        notEqualMessages = null;
        greaterMessages = null;
        lessMessages = null;
        missingMessages = null;
        extraMessages = null;
    }

    public void addEqualMessage(ValuePathMessage message) {
        equalMessages = add(equalMessages, message);
    }

    public void addNotEqualMessage(ValuePathMessage message) {
        notEqualMessages = add(notEqualMessages, message);
    }

    public void addGreaterMessage(ValuePathMessage message) {
        greaterMessages = add(greaterMessages, message);
    }

    public void addLessMessage(ValuePathMessage message) {
        lessMessages = add(lessMessages, message);
    }

    public void addMissingMessage(ValuePathMessage message) {
        missingMessages = add(missingMessages, message);
    }

    public void addExtraMessage(ValuePathMessage message) {
        extraMessages = add(extraMessages, message);
    }

    public ValuePathLazyMessageList getEqualMessages() {
        return equalMessages;
    }

    public ValuePathLazyMessageList getNotEqualMessages() {
        return notEqualMessages;
    }

    public ValuePathLazyMessageList getGreaterMessages() {
        return greaterMessages;
    }

    public ValuePathLazyMessageList getLessMessages() {
        return lessMessages;
    }

    public ValuePathLazyMessageList getMissingMessages() {
        return missingMessages;
    }

    public ValuePathLazyMessageList getExtraMessages() {
        return extraMessages;
    }

    public boolean hasNoExtraAndNoMissing() {
        return isEmpty(extraMessages) && isEmpty(missingMessages);
    }

    public boolean hasExtraOrMissing() {
        return !isEmpty(extraMessages) || !isEmpty(missingMessages);
    }

    public void merge(CompareToResult compareToResult) {
        equalMessages = merge(equalMessages, compareToResult.equalMessages);
        notEqualMessages = merge(notEqualMessages, compareToResult.notEqualMessages);
        greaterMessages = merge(greaterMessages, compareToResult.greaterMessages);
        lessMessages = merge(lessMessages, compareToResult.lessMessages);
        missingMessages = merge(missingMessages, compareToResult.missingMessages);
        extraMessages = merge(extraMessages, compareToResult.extraMessages);
    }

    private ValuePathLazyMessageList merge(ValuePathLazyMessageList parent, ValuePathLazyMessageList child) {
        if (parent == null && child == null) {
            return null;
        }

        if (child == null) {
            return parent;
        }

        if (parent == null) {
            parent = new ValuePathLazyMessageList();
        }

        parent.merge(child);

        return parent;
    }

    private ValuePathLazyMessageList add(ValuePathLazyMessageList list, ValuePathMessage message) {
        if (list == null) {
            list = new ValuePathLazyMessageList();
        }
        list.add(message);
        return list;
    }

    private boolean isEmpty(ValuePathLazyMessageList list) {
        return size(list) == 0;
    }

    private int size(ValuePathLazyMessageList list) {
        return list == null ? 0 : list.size();
    }
}
