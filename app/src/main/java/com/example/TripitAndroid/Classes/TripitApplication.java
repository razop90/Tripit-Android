package com.example.TripitAndroid.Classes;
import android.content.Context;
import androidx.multidex.MultiDexApplication;

public class TripitApplication extends MultiDexApplication {
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
