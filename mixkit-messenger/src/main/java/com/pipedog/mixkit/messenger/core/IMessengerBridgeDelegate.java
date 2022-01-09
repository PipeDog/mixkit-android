package com.pipedog.mixkit.messenger.core;

import com.pipedog.mixkit.messenger.interfaces.IMessage2Server;

public interface IMessengerBridgeDelegate {
    
    /**
     * 向服务端（server）发送消息的实例 caller
     */
    IMessage2Server serverCaller();
    
}
