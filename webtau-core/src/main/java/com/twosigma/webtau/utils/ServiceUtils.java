package com.twosigma.webtau.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class ServiceUtils {
    public static <E> List<E> discover(Class<E> serviceClass) {
        ServiceLoader<E> loader = ServiceLoader.load(serviceClass);
        List<E> services = new ArrayList<E>();
        loader.forEach(services::add);

        return services;
    }
}
