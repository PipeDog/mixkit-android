package com.pipedog.mixkit.launch;

import android.content.Context;

import com.pipedog.mixkit.tool.MixLogger;

public class MixLaunchManager {

    private Context mContext;
    private MixEnvironment mEnv;

    private volatile static MixLaunchManager sDefaultManager;

    public static MixLaunchManager defaultManager() {
        if (sDefaultManager == null) {
            synchronized (MixLaunchManager.class) {
                if (sDefaultManager == null) {
                    sDefaultManager = new MixLaunchManager();
                }
            }
        }
        return sDefaultManager;
    }

    private MixLaunchManager() {
        mEnv = MixEnvironment.Debug;
    }

    public void registerContext(Context ctx) {
        mContext = ctx;
    }

    public Context getContext() {
        if (mContext == null) {
            MixLogger.error("Call method `registerContext` in class `%s` to bind application " +
                    "context before you get context!", this.getClass().getName());
        }
        return mContext;
    }

    public void setEnvironment(MixEnvironment env) {
        mEnv = env;
    }

    public MixEnvironment getEnvironment() {
        return mEnv;
    }

}
