/*
 * Copyright 2024 webtau maintainers
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

import org.testingisdocumenting.webtau.data.ValuePath;

import java.util.*;
import java.util.stream.Stream;

/**
 * maintains only a certain number of messages, but keeps increasing the counter of messages.
 * do not instantiate the underlying list until there is an actual message.
 * Is NOT thread safe
 */
public class ValuePathLazyMessageList implements Iterable<ValuePathMessage> {
    private static final int SIZE_LIMIT = 100;

    private List<ValuePathMessage> messages;
    private ValuePathMessage singleMessage;
    private int size;

    public void add(ValuePathMessage message) {
        size++;

        if (messages == null) {
            if (singleMessage == null) {
                singleMessage = message;
            } else {
                messages = new ArrayList<>();
                messages.add(singleMessage);
                messages.add(message);
                singleMessage = null;
            }
        } else if (size < SIZE_LIMIT) {
            messages.add(message);
        }
    }

    public ValuePathMessage first() {
        if (size == 0) {
            return null;
        } else if (size == 1) {
            return singleMessage;
        } else {
            return messages.get(0);
        }
    }

    public void addAll(Iterable<ValuePathMessage> messages) {
        if (messages == null) {
            return;
        }

        messages.forEach(this::add);
    }

    public void merge(ValuePathLazyMessageList list) {
        for (ValuePathMessage message : list) {
            add(message);
        }
    }

    public void clear() {
        messages = null;
        singleMessage = null;
        size = 0;
    }

    public Set<ValuePath> extractPaths() {
        Set<ValuePath> result = new HashSet<>();
        for (ValuePathMessage v : this) {
            result.add(v.actualPath());
        }

        return result;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Stream<ValuePathMessage> stream() {
        if (size == 0) {
            return Stream.empty();
        } else if (size == 1) {
            return Stream.of(singleMessage);
        } else {
            return messages.stream();
        }
    }

    @Override
    public Iterator<ValuePathMessage> iterator() {
        if (size == 0) {
            return Collections.emptyIterator();
        } else if (size == 1) {
            return Collections.singletonList(singleMessage).iterator();
        } else {
            return messages.iterator();
        }
    }
}
