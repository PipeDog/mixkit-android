package com.pipedog.mixkit.messenger.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ProcessUtils {

    /**
     * 获取当前进程 pid
     */
    public static int getPid() {
        return android.os.Process.myPid();
    }
    
    /**
     * 获取当前进程名称
     */
    public static String getProcessName(Context context) {
        ActivityManager activityManager = 
                (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = 
                activityManager.getRunningAppProcesses();
        
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.pid == getPid()) {
                return appProcess.processName;
            }
        }
        
        return null;
    }

}
