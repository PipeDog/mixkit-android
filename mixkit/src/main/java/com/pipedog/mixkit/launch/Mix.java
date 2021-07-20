package com.pipedog.mixkit.launch;

public class Mix {

    private static MixOptions sOptions;

    public static void launch(MixOptions options) {
        sOptions = options;
    }

    public static MixOptions options() throws Exception {
        if (sOptions == null) {
            throw new Exception("Invoke method `launch` to set options first, or `mixkit` will not work!");
        }
        return sOptions;
    }

}
