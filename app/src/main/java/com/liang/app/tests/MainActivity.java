package com.liang.app.tests;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    private ServiceConnection mServiceConnection;

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
            case R.id.btn_bind_service:
                bindMyService();
                break;
            case R.id.btn_start_service:
                startService();
                break;
            case R.id.btn_content_provider_query:
                contentProviderQuery();
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

    private void bindMyService() {
        if (mServiceConnection != null) {
            Toast.makeText(getApplicationContext(), "服务已经绑定了", Toast.LENGTH_SHORT).show();
            return;
        }

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("MyService", "onServiceConnected");
                try {
                    IMyService.Stub.asInterface(service).hello();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("MyService", "onServiceDisconnected");
            }
        };

        bindService(new Intent(this, MyService.class), mServiceConnection, BIND_AUTO_CREATE);
    }

    private void startService() {
        startService(new Intent(this, MyService.class));
    }

    private void contentProviderQuery() {
        getContentResolver().query(Uri.parse("content://com.liang.app.tests.MyContentProvider"), null, null, null, null);
    }

    @Override
    protected void onDestroy() {
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
        }

        super.onDestroy();
    }
}
