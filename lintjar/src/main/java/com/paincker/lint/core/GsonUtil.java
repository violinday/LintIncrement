package com.paincker.lint.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by haiyang_tan on 2018/7/13.
 */
public class GsonUtil {

    private static Gson sGson = new Gson();

    private GsonUtil() {

    }

    /**
     * String 转换成 bean
     *
     * @param tClass
     * @param value
     * @param <T>
     * @return
     */
    public static <T> T jsonStrToBean(Class<T> tClass, String value) throws Exception {
        return sGson.fromJson(value, tClass);
    }

    public static <T> T jsonStrToBean(Type type, String value) {
        return sGson.fromJson(value, type);
    }

    public static <T> List<T> jsonStrToList(String json, Class clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        return new Gson().fromJson(json, type);
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

}
