package com.paincker.lint.core.config;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lewin on 2018/7/15.
 *
 * Config 工具类
 */
public final class ConfigUtil {
    private ConfigUtil() {}

    /**
     * 获取所以不能常规使用的构造方法
     * @return 构造方法 list
     */
    @Nullable
    public static List<String> getConstructions(@NonNull List<Config> configs) {
        if (configs.isEmpty()) {
            return null;
        }

        List<String> constructions = new ArrayList<>();
        for (Config config : configs) {
            String construction = config.construction;
            if (construction != null && !construction.isEmpty()) {
                constructions.add(construction);
            }
        }
        return  constructions;
    }

    @Nullable
    public static Config getConfigByConstruction(@NonNull String construction, @NonNull List<Config> configs) {
        if (construction.isEmpty() || configs.isEmpty()) {
            return null;
        }

        for (Config config : configs) {
            if (construction.equals(config.construction)) {
                return config;
            }
        }
        return null;
    }

    /**
     * 获取所以不能常规使用的继承父类
     * @return 构造方法 list
     */
    @Nullable
    public static List<String> getSuperClasses(@NonNull List<Config> configs) {
        if (configs.isEmpty()) {
            return null;
        }

        List<String> constructions = new ArrayList<>();
        for (Config config : configs) {
            String construction = config.superClass;
            if (construction != null && !construction.isEmpty()) {
                constructions.add(construction);
            }
        }
        return  constructions;
    }

    @Nullable
    public static Config getConfigBySuperClasss(@NonNull String superClass, @NonNull List<Config> configs) {
        if (superClass.isEmpty() || configs.isEmpty()) {
            return null;
        }

        for (Config config : configs) {
            if (superClass.equals(config.superClass)) {
                return config;
            }
        }
        return null;
    }

    @Nullable
    public static List<String> getMethodClasses(@NonNull List<Config> configs) {
        if (configs.isEmpty()) {
            return null;
        }

        List<String> constructions = new ArrayList<>();
        for (Config config : configs) {
            if (config.method != null && !config.method.isEmpty()) {
                constructions.addAll(config.method);
            }
        }
        return  constructions;
    }

    @Nullable
    public static List<Config> getConfigByMethod(@NonNull String method, @NonNull List<Config> configs) {
        if (method.isEmpty() || configs.isEmpty()) {
            return null;
        }
        List<Config> findConfig = new ArrayList<>();
        for (Config config : configs) {
            if (config.method != null && !config.method.isEmpty()) {
                if (config.method.contains(method)) {
                    findConfig.add(config);
                }
            }
        }
        return findConfig;
    }
}
