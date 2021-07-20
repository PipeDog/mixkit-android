package com.pipedog.mixkit.launch;

import android.content.Context;

public class MixOptions {

    public enum MixEnvironment {
        Debug, Release,
    }

    public Context context;
    public MixEnvironment env;

}
