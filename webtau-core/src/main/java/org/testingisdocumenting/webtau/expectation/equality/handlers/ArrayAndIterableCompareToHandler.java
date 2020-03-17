package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayAndIterableCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual.getClass().isArray() && expected instanceof Iterable;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        List<?> actualList = toList(actual);

        comparator.compareUsingEqualOnly(actualPath, actualList, expected);
    }

    private List<?> toList(Object actual) {
        Class<?> componentType = actual.getClass().getComponentType();
        if (componentType.equals(boolean.class)) {
            return toList((boolean[]) actual);
        } else if (componentType.equals(byte.class)) {
            return toList((byte[]) actual);
        } else if (componentType.equals(char.class)) {
            return toList((char[]) actual);
        } else if (componentType.equals(short.class)) {
            return toList((short[]) actual);
        } else if (componentType.equals(int.class)) {
            return Arrays.stream((int[])actual).boxed().collect(Collectors.toList());
        } else if (componentType.equals(long.class)) {
            return Arrays.stream((long[])actual).boxed().collect(Collectors.toList());
        } else if (componentType.equals(float.class)) {
            return toList((float[]) actual);
        } else if (componentType.equals(double.class)) {
            return Arrays.stream((double[])actual).boxed().collect(Collectors.toList());
        } else {
            return Arrays.stream((Object[])actual).collect(Collectors.toList());
        }
    }

    private List<Boolean> toList(boolean[] booleans) {
        List<Boolean> list = new ArrayList<>(booleans.length);
        for (boolean bool : booleans) {
            list.add(bool);
        }

        return list;
    }

    private List<Byte> toList(byte[] bytes) {
        List<Byte> list = new ArrayList<>(bytes.length);
        for (byte aByte : bytes) {
            list.add(aByte);
        }

        return list;
    }

    private List<Character> toList(char[] chars) {
        List<Character> list = new ArrayList<>(chars.length);
        for (char aChar : chars) {
            list.add(aChar);
        }

        return list;
    }

    private List<Short> toList(short[] shorts) {
        List<Short> list = new ArrayList<>(shorts.length);
        for (short aShort : shorts) {
            list.add(aShort);
        }

        return list;
    }

    private List<Float> toList(float[] floats) {
        List<Float> list = new ArrayList<>(floats.length);
        for (float aFloat : floats) {
            list.add(aFloat);
        }

        return list;
    }
}
