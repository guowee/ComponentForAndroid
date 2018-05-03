package com.muse.router.service;


import java.util.List;


public interface JsonService {

    <T> T parseObject(String text, Class<T> clazz);

    <T> List<T> parseArray(String text, Class<T> clazz);

    String toJsonString(Object instance);

    class Factory {
        private volatile static JsonService jsonService;

        public static JsonService getSingletonImpl() {
            if (jsonService == null) {
                synchronized (Factory.class) {
                    if (jsonService == null)
                        jsonService = new JsonServiceImpl();
                }
            }
            return jsonService;
        }
    }
}
