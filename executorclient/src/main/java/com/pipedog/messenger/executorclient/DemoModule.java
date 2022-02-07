package com.pipedog.messenger.executorclient;

import com.pipedog.mixkit.annotation.MixMethod;
import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.kernel.ResultCallback;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MixModule(name = "DemoModule")
public class DemoModule {

    @MixMethod(name = "testMethod")
    public void testMethod(String str, Integer i, int bi, ResultCallback callback, Map<String, Object> m, List<Object> list) {
        MixLogger.info(">>>>> str = " + str);
        MixLogger.info(">>>>> i = " + i);
        MixLogger.info(">>>>> bi = " + bi);
        MixLogger.info(">>>>> m = " + m.toString());
        MixLogger.info(">>>>> list = " + list.toString());

        List<Object> response = new ArrayList<>();
        response.add(new Integer(200));
        response.add("success");

        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");
        data.put("from", "executorClient");
        response.add(data);

        callback.invoke(response.toArray());
    }

}
