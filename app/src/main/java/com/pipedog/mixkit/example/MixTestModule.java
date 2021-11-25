package com.pipedog.mixkit.example;

import android.util.Log;

import java.util.List;
import java.util.Map;

import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.annotation.MixMethod;
import com.pipedog.mixkit.kernel.MixResultCallback;

@MixModule(name = "MKTestModule")
public class MixTestModule {

    @MixMethod(name = "testExportMethod")
    public void testMethod(String arg1, int arg2, float arg3, MixResultCallback callback,
                           Map<String, String> arg5, List<Object> arg6, Integer testInteger) {

        Log.i("MixTest", ">>>>>> arg1 : " + arg1 + ", arg2 : " + arg2 + ", arg3 float : " + arg3);
        Log.i("MixTest", ">>>>>> arg4, callback : " + callback.toString());
        Log.i("MixTest", ">>>>>> arg5, map : " + arg5.toString()
                        + ", arg6, list : " + arg6.toString());

        Log.i("MixTest", ">>>>>> key1 = " + arg5.get("key1") + ", key2 = " + arg5.get("key2"));
        Log.i("MixTest", ">>>>>> arg6[0] = " + arg6.get(0) + ", arg6[1] = " + arg6.get(1) + ", " +
                "arg6[2] = " + arg6.get(2) + ", arg6[3] = " + arg6.get(3));
        Log.i("MixTest", ">>>>>> testInteger : " + testInteger);

        if (callback != null) {
            Log.i("MixTest", "callback is not null!!!!!!");

            callback.invoke(new Object[]{">>>>>> log finished!!!"});
        }

    }

    @MixMethod(name = "testArgs")
    public void testArgs(Byte b, Short s, Integer i, Long l, Float f, Double d, Boolean bo, Character c) {

    }


}
