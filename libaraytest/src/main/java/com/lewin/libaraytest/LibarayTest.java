package com.lewin.libaraytest;

public class LibarayTest {
    private static class MyThread extends Thread {
        public MyThread(Runnable runnable) {
            super(runnable);
        }
    }
}
