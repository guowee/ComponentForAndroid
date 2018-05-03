package com.muse.router.service;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

class JsonServiceImpl implements JsonService {
    @Override
    public <T> T parseObject(String text, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(text, clazz);
    }

    @Override
    public <T> List<T> parseArray(String text, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(text, new TypeToken<List<T>>() {
        }.getType());
    }

    @Override
    public String toJsonString(Object instance) {
        Gson gson = new Gson();
        return gson.toJson(instance);
    }
}