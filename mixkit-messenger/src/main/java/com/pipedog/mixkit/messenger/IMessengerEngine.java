package com.pipedog.mixkit.messenger;

import android.content.Context;

import com.pipedog.mixkit.kernel.MixResultCallback;

import java.util.Map;

public interface IMessengerEngine {

    // PUBLIC INTERFACES

    public interface IInitialConfiguration {

        /**
         * 设置 app 上下文
         */
        void setContext(Context context);

        /**
         * 设置当前客户端 ID 标识（允许自定义）
         */
        void setClientId(String clientId);

        /**
         * 设置 Action（在 AndroidManifest.xml 文件中配置的 action 值）
         */
        void setAction(String action);

        /**
         * 设置包名（在 AndroidManifest.xml 文件中配置的包名）
         */
        void setPackage(String packageName);

    }

    public interface IConfigurationCallback {
        void setup(IInitialConfiguration configuration);
    }


    // PUBLIC METHODS

    /**
     * 初始化配置
     * @param callback 配置回调
     */
    public void setupConfiguration(IConfigurationCallback callback);

    /**
     * 启动跨进程消息引擎
     * @return true 启动成功，false 启动失败
     **/
    public boolean launch();

    /**
     * 关闭夸进程消息引擎
     */
    public void close();

    /**
     * 发送执行消息到指定客户端
     * @param processId 进程 ID
     * @param moduleName 模块名称
     * @param methodName 方法名
     * @param parameter 参数包装实例
     * @param callback 消息结果回调
     */
    public void sendMessage(String processId,
                            String moduleName,
                            String methodName,
                            Map<String, Object> parameter,
                            MixResultCallback callback);

    /**
     * 获取当前 app 上下文
     */
    public Context getContext();

    /**
     * 获取当前客户端 ID
     */
    public String getClientId();

    /**
     * 获取绑定服务端 Action 信息
     */
    public String getAction();

    /**
     * 获取服务端包名
     */
    public String getPackage();

}
