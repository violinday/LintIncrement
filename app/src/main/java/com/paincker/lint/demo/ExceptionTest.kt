package com.paincker.lint.demo

/**
 * Created by Lewin on 2018/7/22.
 */
class ExceptionTest {

    companion object  {

        fun showSomething() {
            if (true) {
                // Maybe throw a exception
                throw RuntimeException()
            }
        }

    }
}