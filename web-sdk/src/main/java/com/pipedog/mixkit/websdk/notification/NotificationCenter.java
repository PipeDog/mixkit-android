package com.pipedog.mixkit.websdk.notification;

import com.pipedog.mixkit.web.MixWebView;
import com.pipedog.mixkit.websdk.constants.NotificationConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Web 容器的通知中心
 * @author liang
 * @time 2021/12/26
 */
public class NotificationCenter {

    private Map<String, String> mRegisterTable = new HashMap<>();
    private List<MixWebView> mObservers = new ArrayList<>();
    private volatile static NotificationCenter sNotificationCenter;

    public static NotificationCenter getInstance() {
        if (sNotificationCenter == null) {
            synchronized (NotificationCenter.class) {
                if (sNotificationCenter == null) {
                    sNotificationCenter = new NotificationCenter();
                }
            }
        }
        return sNotificationCenter;
    }


    // PRIVATE METHODS

    private NotificationCenter() {
        registerDefaultNotifications();
    }

    private void registerDefaultNotifications() {
        registerNotification(
                NotificationConstants.Name.PAGE_VISIBLE,
                NotificationConstants.JSFunction.ON_PAGE_VISIBLE);
        registerNotification(
                NotificationConstants.Name.PAGE_INVISIBLE,
                NotificationConstants.JSFunction.ON_PAGE_INVISIBLE);
        registerNotification(
                NotificationConstants.Name.PAGE_DESTROY,
                NotificationConstants.JSFunction.ON_PAGE_DESTROY);
    }


    // PUBLIC METHODS

    /**
     * 注册通知并关联 js 函数（当触发该通知时会调用关联的 js 函数）
     * @param notification 通知名称
     * @param jsFunc js 函数名称
     */
    public void registerNotification(String notification, String jsFunc) {
        if (notification == null || notification.isEmpty()) { return; }
        if (jsFunc == null || jsFunc.isEmpty()) { return; }

        mRegisterTable.put(notification, jsFunc);
    }

    /**
     * 注销通知及关联的 js 函数
     * @param notification 通知名称
     */
    public void unregisterNotification(String notification) {
        if (notification == null) {
            return;
        }
        mRegisterTable.remove(notification);
    }

    /**
     * 发出通知，并传递相关参数
     * @param notification 通知名
     * @param userInfo 通知参数
     * @param toWebView 目标 web 视图，如果不为 null 则只有该视图执行关联的 js
     *                  函数，如果为 null 则所有 web 视图都执行关联的 js 函数
     */
    public void postNotification(String notification, Object[] arguments, MixWebView toWebView) {
        String jsFunc = mRegisterTable.get(notification);
        if (jsFunc == null || jsFunc.isEmpty()) {
            return;
        }

        if (toWebView == null) {
            for (MixWebView observer : mObservers) {
                observer.invokeMethod(jsFunc, arguments, null);
            }
            return;
        }

        if (mObservers.contains(toWebView)) {
            toWebView.invokeMethod(jsFunc, arguments, null);
        }
    }

    /**
     * 添加 webView 监听者
     * @param webView web 视图，当调用 `postNotification` 方法进行
     *        通知时将会根据通知名，通过 webView 执行关联的 js 函数
     */
    public void addObserver(MixWebView webView) {
        if (webView == null || mObservers.contains(webView)) {
            return;
        }
        mObservers.add(webView);
    }

    /**
     * 移除 webView 监听者
     * @param webView web 视图
     */
    public void removeObserver(MixWebView webView) {
        if (webView == null || !mObservers.contains(webView)) {
            return;
        }
        mObservers.remove(webView);
    }

    /**
     * 清理所有 webView 监听
     */
    public void removeAllObservers() {
        mObservers.clear();
    }

}
