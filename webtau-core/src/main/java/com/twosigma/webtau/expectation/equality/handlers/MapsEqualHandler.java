package com.twosigma.webtau.expectation.equality.handlers;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;

import java.util.*;

public class MapsEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof Map && expected instanceof Map;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        Map<?, ?> actualMap = (Map) actual;
        Map<?, ?> expectedMap = (Map) expected;

        Comparator comparator = new Comparator(equalComparator, actualPath, actualMap, expectedMap);
        comparator.compare();
    }

    private class Comparator {
        private EqualComparator equalComparator;
        private ActualPath actualPath;
        private Map<?, ?> actualMap;
        private Map<?, ?> expectedMap;
        private Set<Object> allKeys;

        Comparator(EqualComparator equalComparator, ActualPath actualPath, Map<?, ?> actualMap, Map<?, ?> expectedMap) {
            this.equalComparator = equalComparator;
            this.actualPath = actualPath;
            this.actualMap = actualMap;
            this.expectedMap = expectedMap;

            allKeys = new HashSet<>(actualMap.keySet());
            allKeys.addAll(expectedMap.keySet());
        }

        void compare() {
            allKeys.forEach(this::handleKey);
        }

        private void handleKey(Object key) {
            ActualPath propertyPath = actualPath.property(key.toString());

            if (! actualMap.containsKey(key)) {
                equalComparator.reportMissing(MapsEqualHandler.this, propertyPath, expectedMap.get(key));
            } else if (! expectedMap.containsKey(key)) {
                equalComparator.reportExtra(MapsEqualHandler.this, propertyPath, actualMap.get(key));
            } else {
                equalComparator.compare(propertyPath, actualMap.get(key), expectedMap.get(key));
            }
        }
    }
}
