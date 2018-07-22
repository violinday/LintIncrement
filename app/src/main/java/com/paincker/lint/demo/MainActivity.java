package com.paincker.lint.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("tag", "msg");

        ConstructionTest.testThread();
        ConstructionTest.testSuperThread();

        ExceptionTest.Companion.showSomething();

    }


}
