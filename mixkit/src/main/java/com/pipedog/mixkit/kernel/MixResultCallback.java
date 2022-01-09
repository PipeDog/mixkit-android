package com.pipedog.mixkit.kernel;

import java.util.List;

/**
 * 自定义 module 中的 callback 接口定义
 * @author liang
 */
public interface MixResultCallback {
    public void invoke(Object[] response);
}
