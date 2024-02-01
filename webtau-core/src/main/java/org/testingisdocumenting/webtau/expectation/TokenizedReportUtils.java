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

package org.testingisdocumenting.webtau.expectation;

import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.ValuePathLazyMessageList;
import org.testingisdocumenting.webtau.expectation.equality.ValuePathMessage;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class TokenizedReportUtils {
    private TokenizedReportUtils() {
    }

    public static TokenizedMessage generateReportPart(ValuePath topLevelActualPath, TokenizedMessage label, List<ValuePathLazyMessageList> messagesGroups) {
        if (messagesGroups.stream().allMatch(l -> l == null || l.isEmpty())) {
            return tokenizedMessage();
        }

        return tokenizedMessage().add(label).colon().doubleNewLine().add(
                generateReportPartWithoutLabel(topLevelActualPath, messagesGroups.stream()));
    }

    public static TokenizedMessage combineReportParts(TokenizedMessage... parts) {
        TokenizedMessage result = tokenizedMessage();

        List<TokenizedMessage> nonEmpty = Arrays.stream(parts)
                .filter(part -> !part.isEmpty())
                .toList();

        int idx = 0;
        for (TokenizedMessage message : nonEmpty) {
            boolean isLast = idx == nonEmpty.size() - 1;

            result.add(message);
            if (!isLast) {
                result.doubleNewLine();
            }

            idx++;
        }

        return result;
    }

    public static TokenizedMessage generateReportPartWithoutLabel(ValuePath topLevelActualPath,
                                                                  Stream<ValuePathLazyMessageList> messagesGroupsStream) {
        return generateReportPartWithoutLabel(topLevelActualPath, messagesGroupsStream, WebTauConfig.getCfg().getMatchersReportEntriesLimit());
    }

    public static TokenizedMessage generateReportPartWithoutLabel(ValuePath topLevelActualPath,
                                                                  Stream<ValuePathLazyMessageList> messagesGroupsStream,
                                                                  int maxNumberOfEntries) {
        List<ValuePathLazyMessageList> messagesGroups = messagesGroupsStream.filter(group -> group != null && !group.isEmpty()).toList();
        if (messagesGroups.isEmpty()) {
            return tokenizedMessage();
        }

        TokenizedMessage result = tokenizedMessage();
        int groupIdx = 0;
        for (ValuePathLazyMessageList group : messagesGroups) {
            TokenizedReportUtils.appendToReport(result, topLevelActualPath, group, maxNumberOfEntries);

            boolean isLastGroup = groupIdx == messagesGroups.size() - 1;
            if (!isLastGroup) {
                result.newLine();
            }

            groupIdx++;
        }

        return result;
    }

    private static void appendToReport(TokenizedMessage report,
                                       ValuePath topLevelActualPath,
                                       ValuePathLazyMessageList messages,
                                       int maxNumberOfEntries) {
        boolean needToLimit = messages.size() > maxNumberOfEntries;
        int messageIdx = 0;
        for (ValuePathMessage message : messages) {
            boolean reachedLimit = needToLimit && messageIdx == maxNumberOfEntries;
            if (reachedLimit) {
                report.delimiter("...");
                return;
            }

            boolean useFullMessage = !message.actualPath().equals(topLevelActualPath);
            report.add(useFullMessage ? message.buildFullMessage() : message.buildMessage());

            boolean isLast = messageIdx == messages.size() - 1;
            if (!isLast) {
                report.newLine();
            }
            messageIdx++;
        }
    }
}
