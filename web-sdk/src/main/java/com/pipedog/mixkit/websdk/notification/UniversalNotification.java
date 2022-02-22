package com.pipedog.mixkit.websdk.notification;


/**
 * 通用内置通知定义
 * @author liang
 * @time 2021/12/26
 */
public interface UniversalNotification {

    /**
     * 通知名称定义
     */
    public interface Name {

        // PAGE NOTIFICATIONS

        /**
         * 页面进入可见状态，绑定函数为 {@link JSFunction#ON_PAGE_VISIBLE}
         */
        public static final String PAGE_VISIBLE = "PAGE_VISIBLE";

        /**
         * 页面进入不可见状态，绑定函数为 {@link JSFunction#ON_PAGE_INVISIBLE}
         */
        public static final String PAGE_INVISIBLE = "PAGE_INVISIBLE";

        /**
         * 页面将要被销毁，绑定函数为 {@link JSFunction#ON_PAGE_DESTROY}
         */
        public static final String PAGE_DESTROY = "PAGE_DESTROY";

    }

    /**
     * 通知绑定的 js 函数名
     */
    public interface JSFunction {

        // PAGE NOTIFICATIONS

        /**
         * 页面进入可见状态
         */
        public static final String ON_PAGE_VISIBLE = "window.onPageVisible";

        /**
         * 页面进入不可见状态
         */
        public static final String ON_PAGE_INVISIBLE = "window.onPageInvisible";

        /**
         * 页面将要被销毁
         */
        public static final String ON_PAGE_DESTROY = "window.onPageDestroy";

    }

}