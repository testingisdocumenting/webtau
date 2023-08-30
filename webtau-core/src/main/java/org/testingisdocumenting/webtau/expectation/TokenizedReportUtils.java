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

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.ValuePathMessage;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class TokenizedReportUtils {
    private TokenizedReportUtils() {
    }

    public static TokenizedMessage generateReportPart(ValuePath topLevelActualPath, TokenizedMessage label, List<List<ValuePathMessage>> messagesGroups) {
        if (messagesGroups.stream().allMatch(List::isEmpty)) {
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

    public static TokenizedMessage generateReportPartWithoutLabel(ValuePath topLevelActualPath, Stream<List<ValuePathMessage>> messagesGroupsStream) {
        List<List<ValuePathMessage>> messagesGroups = messagesGroupsStream.filter(group -> !group.isEmpty()).toList();
        if (messagesGroups.isEmpty()) {
            return tokenizedMessage();
        }

        TokenizedMessage result = tokenizedMessage();
        int groupIdx = 0;
        for (List<ValuePathMessage> group : messagesGroups) {
            TokenizedReportUtils.appendToReport(result, topLevelActualPath, group);

            boolean isLastGroup = groupIdx == messagesGroups.size() - 1;
            if (!isLastGroup) {
                result.newLine();
            }

            groupIdx++;
        }

        return result;
    }

    public static TokenizedMessage appendToReport(TokenizedMessage report, ValuePath topLevelActualPath, List<ValuePathMessage> messages) {
        int messageIdx = 0;
        for (ValuePathMessage message : messages) {
            boolean useFullMessage = !message.getActualPath().equals(topLevelActualPath);
            report.add(useFullMessage ? message.getFullMessage() : message.getMessage());

            boolean isLast = messageIdx == messages.size() - 1;
            if (!isLast) {
                report.newLine();
            }
            messageIdx++;
        }

        return report;
    }
}
