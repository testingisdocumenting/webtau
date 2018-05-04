package com.twosigma.webtau.utils;

import java.text.NumberFormat;
import java.text.ParseException;

public class NumberUtils {
    private NumberUtils() {
    }

    public static Number convertStringToNumber(String text) {
        try {
            return NumberFormat.getInstance().parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
