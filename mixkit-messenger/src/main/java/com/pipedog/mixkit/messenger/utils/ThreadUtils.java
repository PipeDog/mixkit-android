package com.pipedog.mixkit.messenger.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

public class ThreadUtils {

    /**
     * 在主线程执行任务
     * @param r 将要被执行的任务代码
     */
    public static void runInMainThread(@NonNull Runnable r) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            r.run();
        } else {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(r);
        }
    }

}
