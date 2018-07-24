package com.lewin.lint.demo;

public class ExceptionTest {

    static void doSomething() {
        if (true)
            throw new RuntimeException("oh ~, we have exception");
    }
}
