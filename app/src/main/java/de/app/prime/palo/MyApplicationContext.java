package de.app.prime.palo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;


public class MyApplicationContext extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public void onCreate() {
        super.onCreate();
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
        MyApplicationContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplicationContext.context;
    }
}