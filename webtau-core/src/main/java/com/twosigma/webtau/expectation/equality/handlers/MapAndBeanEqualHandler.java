package com.twosigma.webtau.expectation.equality.handlers;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;
import com.twosigma.webtau.utils.JavaBeanUtils;

import java.util.Map;

public class MapAndBeanEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return isMapOfProps(expected) && isBean(actual);
    }

    @SuppressWarnings("unchecked")
    private boolean isMapOfProps(Object o) {
        if (!(o instanceof Map)) {
            return false;
        }

        return ((Map) o).keySet().stream().allMatch(k -> k instanceof String);
    }

    private boolean isBean(Object o) {
        if (o instanceof Iterable || o instanceof Map) {
            return false;
        }

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        Map<String, ?> expectedMap = (Map<String, ?>) expected;
        Map<String, ?> actualAsMap = JavaBeanUtils.convertBeanToMap(actual);

        expectedMap.keySet().forEach(p -> {
            ActualPath propertyPath = actualPath.property(p);

            if (! actualAsMap.containsKey(p)) {
                equalComparator.reportMissing(this, propertyPath, expectedMap.get(p));
            } else {
                equalComparator.compare(propertyPath, actualAsMap.get(p), expectedMap.get(p));
            }
        });
    }
}
