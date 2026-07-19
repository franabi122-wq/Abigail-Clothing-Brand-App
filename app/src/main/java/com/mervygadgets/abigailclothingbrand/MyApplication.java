package com.mervygadgets.abigailclothingbrand;

import android.app.Application;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Access the Debug namespace directly
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);

        // Initialize with your App ID
        OneSignal.initWithContext(this, "de5f4c57-afeb-4123-aebc-e0882876ca4e");
    }
}
