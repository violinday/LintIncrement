package com.paincker.lint.core.config;

/**
 * Created by haiyang_tan on 2018/7/13.
 */
public class Config {

    // 禁止构建的类
    public String construction;
    // 禁止继承的类
    public String superClass;
    // 禁止的调用的属性
    public String feild;
    // 禁止调用的方法
    public String method;


    // error信息
    public String message;

    // 如果可以调用需要handle的Exception，如果没有这个属性，则认为是不能调用的
    public String exception;

    // 错误级别
    public String severity;
}
