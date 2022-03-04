package com.pipedog.mixkit.websdk.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.View;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.pipedog.mixkit.web.interfaces.IMixWebView;
import com.pipedog.mixkit.websdk.notification.NotificationCenter;
import com.pipedog.mixkit.websdk.notification.UniversalNotification;

/**
 * Web 生命周期管理
 */
public class WebLifecycleObserver {

    private IMixWebView mWebView;
    private Activity mActivity;
    private WebActivityLifecycleCallbacks mLifecycleCallbacks;

    private WebLifecycleObserver() {

    }

    public WebLifecycleObserver(IMixWebView webView) {
        if (webView == null || !(webView instanceof View)) {
            throw new RuntimeException("Invalid argument `webView` from `WebLifecycleObserver(IMixWebView)`, check it!");
        }

        mWebView = webView;
    }


    // PUBLIC METHODS

    /**
     * 订阅上下文生命周期
     */
    public void observe() {
        if (mLifecycleCallbacks != null) {
            return;
        }

        mActivity = ContextUtils.getActivity(((View) mWebView).getContext());
        if (mActivity == null) {
            return;
        }

        mLifecycleCallbacks = new WebActivityLifecycleCallbacks();
        mActivity.registerActivityLifecycleCallbacks(mLifecycleCallbacks);
    }

    /**
     * 取消监听订阅
     */
    public void cancel() {
        if (mLifecycleCallbacks == null) {
            return;
        }

        mActivity.unregisterActivityLifecycleCallbacks(mLifecycleCallbacks);
        mActivity = null;
        mLifecycleCallbacks = null;
    }


    // PRIVATE CLASSES

    private class WebActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            if (mActivity == activity) {
                NotificationCenter.getInstance().postNotification(
                        UniversalNotification.Name.PAGE_VISIBLE, new Object[]{}, mWebView);
            }
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            if (mActivity == activity) {
                NotificationCenter.getInstance().postNotification(
                        UniversalNotification.Name.PAGE_INVISIBLE, new Object[]{}, mWebView);
            }
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            if (mActivity == activity) {
                NotificationCenter.getInstance().postNotification(
                        UniversalNotification.Name.PAGE_DESTROY, new Object[]{}, mWebView);
            }
        }
    }

}
