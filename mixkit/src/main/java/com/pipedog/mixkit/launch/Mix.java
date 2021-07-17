package com.pipedog.mixkit.launch;

public class Mix {

    private static MixOptions mOptions;

    public static void launch(MixOptions options) {
        mOptions = options;
    }

    public static MixOptions options() throws Exception {
        if (mOptions == null) {
            throw new Exception("Invoke method `launch` to set options first, or `mixkit` will not work!");
        }
        return mOptions;
    }

}
