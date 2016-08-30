package com.liang.app.tests;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archer on 2016/8/9.
 */
public class HelloActivity extends AppCompatActivity {
    @Override
    public synchronized boolean dispatchKeyEvent(KeyEvent event) {
        List<? super Number> aa = new ArrayList<>();
        List<Object> bb = new ArrayList<>();

        aa = bb;

        Object o = aa.get(0);
        aa.add(new Integer(1));

        return super.dispatchKeyEvent(event);
    }

    public static class Klass<T extends Activity> {

    }
}
