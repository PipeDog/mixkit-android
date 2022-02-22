package com.pipedog.mixkit.websdk.interfaces;

import java.util.Map;

/**
 * 业务层直接使用的 WebView 会实现此接口
 */
public interface IOpenWebView extends IWebView {

    /**
     * 设置一些额外绑定的业务参数，你可以利用这个方法在自定义的 module 中获
     * 取到所需要的业务参数，比如，你可以通过这个方法来进行 Fragment 的绑定
     * 注意：
     *  这将大大提升代码的耦合性，一定要慎重使用
     */
    void setExtraData(Map<String, Object> extraData);

    /**
     * 获取通过绑定的业务参数
     */
    Map<String, Object> getExtraData();

}
