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
    private Lifecycle mLifecycle;
    private LifecycleEventObserver mLifecycleObserver;

    public WebLifecycleObserver(IMixWebView webView) {
        mWebView = webView;
    }

    private WebLifecycleObserver() {

    }


    // PUBLIC METHODS

    /**
     * 订阅上下文生命周期 前提 context 继承父类必须继承了 LifecycleOwner
     */
    public void observe() {
        if (mLifecycle != null) {
            return;
        }

        mLifecycle = getLifeCycle(((View) mWebView).getContext());
        if (mWebView == null || !(mWebView instanceof View)) {
            return;
        }

        View webView = (View) mWebView;
        mLifecycle = getLifeCycle(webView.getContext());

        if (mLifecycle == null) {
            throw new RuntimeException(
                    "The activity must be kind of class `ComponentActivity`!");
        }

        if (mLifecycleObserver == null) {
            mLifecycleObserver = new LifecycleEventObserverImpl();
        }

        mLifecycle.addObserver(mLifecycleObserver);
    }

    /**
     * 取消监听订阅
     */
    public void cancel () {
        if (mLifecycle == null) {
            return;
        }

        mLifecycle.removeObserver(mLifecycleObserver);
        mLifecycle = null;
        mLifecycleObserver = null;
    }


    // PRIVATE METHODS

    /**
     * Context 来源类型类型如果继承自 ComponentActivity 可以拿到 Lifecycle
     */
    private Lifecycle getLifeCycle(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof ComponentActivity) {
                return ((ComponentActivity) context).getLifecycle();
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }


    // PRIVATE CLASSES

    /**
     * 生命周期监听
     */
    private class LifecycleEventObserverImpl implements LifecycleEventObserver {
        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            switch (event) {
                case ON_RESUME: {
                    NotificationCenter.getInstance().postNotification(
                            UniversalNotification.Name.PAGE_VISIBLE, new Object[]{}, mWebView);
                } break;
                case ON_PAUSE: {
                    NotificationCenter.getInstance().postNotification(
                            UniversalNotification.Name.PAGE_INVISIBLE, new Object[]{}, mWebView);
                } break;
                case ON_DESTROY: {
                    NotificationCenter.getInstance().postNotification(
                            UniversalNotification.Name.PAGE_DESTROY, new Object[]{}, mWebView);
                } break;
                default: break;
            }
        }
    }

}
