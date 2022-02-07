package com.pipedog.mixkit.websdk.manager;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.pipedog.mixkit.web.MixWKWebView;
import com.pipedog.mixkit.websdk.constants.NotificationConstants;
import com.pipedog.mixkit.websdk.notification.NotificationCenter;

public class WebLifecycleObserver {

    private MixWKWebView mWebView;
    private Lifecycle mLifecycle;
    private LifecycleEventObserver mLifecycleObserver;

    public WebLifecycleObserver(MixWKWebView webView) {
        mWebView = webView;
    }

    private WebLifecycleObserver() {

    }


    // PUBLIC METHODS

    /**
     * 订阅上下文生命周期 前提 context 继承父类必须继承了 LifecycleOwner
     */
    public void observe() {
        mLifecycle = getLifeCycle(mWebView.getContext());
        if (mLifecycle == null) {
            throw new RuntimeException(
                    "未检测到继承 LifecycleOwner Context 暂不支持生命周期监听推荐使用 ComponentActivity");
        }

        if (mLifecycleObserver == null) {
            mLifecycleObserver = new LifecycleEventObserverImpl();
        }

        mLifecycle.addObserver(mLifecycleObserver);
    }

    /**
     * 取消监听订阅
     */
    public void cancel() {
        mLifecycle.removeObserver(mLifecycleObserver);
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
                            NotificationConstants.Name.PAGE_VISIBLE, new Object[]{}, mWebView);
                } break;
                case ON_PAUSE: {
                    NotificationCenter.getInstance().postNotification(
                            NotificationConstants.Name.PAGE_INVISIBLE, new Object[]{}, mWebView);
                } break;
                case ON_DESTROY: {
                    NotificationCenter.getInstance().postNotification(
                            NotificationConstants.Name.PAGE_DESTROY, new Object[]{}, mWebView);
                } break;
                default: break;
            }
        }
    }

}
