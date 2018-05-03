package com.muse.router.service;

import android.os.Bundle;

import com.muse.router.exceptions.ParamException;


public interface AutowiredService {
    boolean THROW_CONFIG = true;


    void autowire(Object instance);

    void preCondition(Class targetActivityClz, Bundle params) throws ParamException;

    class Factory {
        private volatile static AutowiredService autowiredServiceImpl;

        public static AutowiredService getSingletonImpl() {
            if (autowiredServiceImpl == null) {
                synchronized (AutowiredService.class) {
                    if (autowiredServiceImpl == null)
                        autowiredServiceImpl = new AutowiredServiceImpl();
                }
            }
            return autowiredServiceImpl;
        }
    }
}
