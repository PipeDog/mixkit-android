package com.pipedog.mixkit.tool;

import android.os.Handler;
import android.os.Looper;

/**
 * 线程工具
 * @author liang
 * @time 2022/01/10
 */
public class ThreadUtils {

    /**
     * 在主线程执行任务
     * @param r 将要被执行的任务代码
     */
    public static void runInMainThread(Runnable r) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            r.run();
        } else {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(r);
        }
    }

}
