package com.twosigma.webtau.utils;

import java.util.*;

public class ServiceLoaderUtils {
    private ServiceLoaderUtils() {
    }

    public static <E> Set<E> load(Class<E> serviceClass) {
        ServiceLoader<E> loader = ServiceLoader.load(serviceClass);
        Set<E> result = new LinkedHashSet<>();
        loader.forEach(result::add);

        return result;
    }
}
