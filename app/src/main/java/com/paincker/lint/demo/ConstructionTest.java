package com.paincker.lint.demo;

public class ConstructionTest {

    public static void testThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).run();
    }

    public static void testSuperThread() {
        new MyThread(new Runnable(){
            @Override
            public void run() {

            }
        }).run();
    }

    public static void testSuperThread2() {
        new MyThread(new Runnable(){
            @Override
            public void run() {

            }
        }).run();
    }

    private static class MyThread extends Thread {
        public MyThread(Runnable runnable) {
            super(runnable);
        }
    }
}
