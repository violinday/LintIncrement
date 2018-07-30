package com.lewin.lint.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("tag", "msg");

        ConstructionTest.testThread();
        ConstructionTest.testSuperThread();

        try {
            ExceptionTest.doSomething();
        }catch (RuntimeException e) {

        }

        String a = "111,111";
        long b = Long.parseLong(a);

    }


}
