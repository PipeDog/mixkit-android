package com.pipedog.mixkit.example;

import android.content.Context;
import android.app.Application;

import com.pipedog.mixkit.launch.MixEnvironment;
import com.pipedog.mixkit.launch.MixLaunchManager;

public class MixApplication extends Application {

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this.getApplicationContext();

        MixLaunchManager launchManager = MixLaunchManager.defaultManager();
        launchManager.registerContext(mContext);
        launchManager.setEnvironment(MixEnvironment.Debug);
    }

}
