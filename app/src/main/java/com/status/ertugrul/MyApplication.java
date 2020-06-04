package com.status.ertugrul;

import android.app.Application;
import android.content.Context;

import com.splunk.mint.Mint;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Mint.initAndStartSession(this, "7624c5f1");
        Mint.disableNetworkMonitoring();

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
}
