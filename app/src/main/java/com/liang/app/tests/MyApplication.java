package com.liang.app.tests;

import android.content.Context;
import android.util.Log;


/**
 * Created by Archer on 2016/8/12.
 */
public class MyApplication extends android.app.Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d("life", "attachBaseContext");
    }
}
