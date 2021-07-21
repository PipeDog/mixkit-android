package com.pipedog.mixkit.example;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.annotation.MixMethod;
import com.pipedog.mixkit.kernel.MixResultCallback;

@MixModule(name = "MKTestModule")
public class MixTestModule {

    @MixMethod(name = "testExportMethod")
    public void testMethod(String arg1, int arg2, float arg3, MixResultCallback callback) {

        Log.i("MixTest", ">>>>>> arg1 : " + arg1 + ", arg2 : " + arg2 + ", arg3 float : " + arg3);
        Log.i("MixTest", ">>>>>> arg4, callback : %s" + callback.toString());

        if (callback != null) {
            Log.i("MixTest", "callback is not null!!!!!!");

            List<Object> args = new ArrayList<Object>();
            args.add(">>>>>> log finished!");
            callback.invoke(args);
        }

    }
}
