package com.pipedog.mixkit.example;

import android.content.Context;
import android.app.Application;
import com.pipedog.mixkit.launch.Mix;
import com.pipedog.mixkit.launch.MixOptions;

public class MixApplication extends Application {

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this.getApplicationContext();

        MixOptions options = new MixOptions();
        options.context = mContext;
        options.env = MixOptions.MixEnvironment.Debug;
        Mix.launch(options);
    }

}
