package com.liang.app.tests;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_hook_activity_thread_handle:
                hookActivityMainThreadHandler2();
                break;
            case R.id.btn_start_hello_activity:
                startActivity(new Intent(this, HelloActivity.class));
                break;
            case R.id.btn_print_looper_message:
                printLooperMessage();
                break;
        }
    }

    private void hookActivityMainThreadHandler2() {
        try {
            Class<?> clazz = Class.forName("android.app.ActivityThread");
            Method method = clazz.getDeclaredMethod("currentActivityThread");
            Object currentActivityThread = method.invoke(null);

            Field field = clazz.getDeclaredField("mH");
            field.setAccessible(true);
            Object h = field.get(currentActivityThread);

            field = Handler.class.getDeclaredField("mCallback");
            field.setAccessible(true);
            field.set(h, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    Log.d("handleMessage", msg.toString());
                    return false;
                }
            });

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void printLooperMessage() {
        Looper.getMainLooper().setMessageLogging(new LogPrinter(Log.DEBUG, "looper"));
    }
    
}
