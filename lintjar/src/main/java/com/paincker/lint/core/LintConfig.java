package com.paincker.lint.core;

import com.android.tools.lint.detector.api.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by haiyang_tan on 2018/7/13.
 */
public final class LintConfig {

    public static final String TAG = "LintConfig";

    public static final String LINT_CONFIG_FILE = "tt-lint-config.json";

    private final List<Config> mConfigs;

    LintConfig(Context context) {
        File projectDir = context.getProject().getDir();
        File configFile = new File(projectDir, LINT_CONFIG_FILE);
        // 如果不存在
        if (!configFile.exists() || !configFile.isFile()) {
            // 采用main project的
            File mainProjectDir = context.getMainProject().getDir();
            configFile = new File(mainProjectDir, LINT_CONFIG_FILE);
            if (!configFile.exists() || !configFile.isFile()) {
                throw new RuntimeException("not find config file !");
            }
        }
        String strConfig = readFile(configFile);
        mConfigs = GsonUtil.jsonStrToList(strConfig, Config.class);
    }

    private static String readFile(File file) {
        BufferedReader reader = null;
        StringBuilder laststr = new StringBuilder();
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                laststr.append(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return laststr.toString();
    }
}
