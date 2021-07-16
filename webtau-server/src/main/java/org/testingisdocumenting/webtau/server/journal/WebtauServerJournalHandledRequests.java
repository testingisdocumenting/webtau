/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.server.journal;

import org.testingisdocumenting.webtau.data.live.LiveValue;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;

/**
 * convenient way to handle <code>waitTo</code> on calls
 */
public class WebtauServerJournalHandledRequests {
    public final HandledRequestsLiveValue GET;
    public final HandledRequestsLiveValue POST;
    public final HandledRequestsLiveValue PUT;
    public final HandledRequestsLiveValue DELETE;
    public final HandledRequestsLiveValue PATCH;

    public WebtauServerJournalHandledRequests(WebtauServerJournal journal) {
        this.GET = new HandledRequestsLiveValue(journal, "GET");
        this.POST = new HandledRequestsLiveValue(journal, "POST");
        this.PUT = new HandledRequestsLiveValue(journal, "PUT");
        this.DELETE = new HandledRequestsLiveValue(journal, "DELETE");
        this.PATCH = new HandledRequestsLiveValue(journal, "PATCH");
    }

    public static class HandledRequestsLiveValue implements
            ActualValueExpectations,
            ActualValueAware,
            ActualPathAndDescriptionAware {
        final ActualPath actualPath;

        final WebtauServerJournal journal;
        final String method;

        private HandledRequestsLiveValue(WebtauServerJournal journal, String method) {
            this.journal = journal;
            this.method = method;
            this.actualPath = new ActualPath(journal.getServerId());
        }

        public Object get() {
            return journal.handledRequestsByMethod(method).stream()
                    .map(WebtauServerHandledRequest::getUrl)
                    .collect(Collectors.toList());
        }

        @Override
        public ActualPath actualPath() {
            return actualPath;
        }

        @Override
        public LiveValue<Object> actualValue() {
            return HandledRequestsLiveValue.this::get;
        }

        @Override
        public TokenizedMessage describe() {
            return TokenizedMessage.tokenizedMessage(classifier("server"),
                    IntegrationTestsMessageBuilder.id(journal.getServerId()), classifier(method), action("handled calls"));
        }
    }
}
