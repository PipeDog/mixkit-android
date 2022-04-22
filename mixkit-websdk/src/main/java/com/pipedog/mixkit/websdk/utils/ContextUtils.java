package com.pipedog.mixkit.websdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

/**
 * Context 工具集合
 */
public class ContextUtils {

    /**
     * 通过 context 获取 Activity 实例
     */
    public static Activity getActivity(Context context) {
        if (context == null) {
            return null;
        }

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        return null;
    }

}
