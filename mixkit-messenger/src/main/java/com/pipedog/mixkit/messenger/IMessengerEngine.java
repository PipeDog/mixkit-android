package com.pipedog.mixkit.messenger;

import android.content.Context;

import com.pipedog.mixkit.kernel.MixResultCallback;

import java.util.Map;

/**
 * 跨进程通信引擎接口
 * @author liang
 * @time 2021/11/23
 *
 * 服务端进程需要在 AndroidManifest.xml 中进行如下配置：
 *  <p>
 *  <manifest xmlns:android="http://schemas.android.com/apk/res/android"
 *     package="com.xxx.module">
 *
 *      <service
 *          android:name="com.pipedog.mixkit.messenger.server.MessengerService"
 *          android:enabled="true"
 *          android:exported="true">
 *              <intent-filter>
 *                  <action android:name="com.xxx.service"></action>
 *                  <category android:name="android.intent.category.DEFAULT" />
 *              </intent-filter>
 *      </service>
 *
 *
 *  </manifest>
 *  </p>
 *
 * @NOTE:
 *  1、在 `manifest` 节点下指定的 `package` 参数需要在初始化引擎时做为参数传入
 *      IMessengerEngine.IInitialConfiguration -> void setAction(String)
 *
 *  2、在 `service` 节点下指定的 `action` 参数需要在初始化引擎室做为参数传入
 *      IMessengerEngine.IInitialConfiguration -> void setPackage(String)
 *
 */
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
         * 设置 Action（在服务端进程的 AndroidManifest.xml 文件中配置的 action 值）
         */
        void setAction(String action);

        /**
         * 设置服务进程包名（在服务端进程 AndroidManifest.xml 文件中配置的包名）
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
     * 关闭跨进程消息引擎
     */
    public void close();

    /**
     * 发送执行消息到指定客户端
     * @param clientId 目标客户端 ID
     * @param moduleName 模块名称
     * @param methodName 方法名
     * @param parameter 参数包装实例
     * @param callback 消息结果回调
     */
    public void sendMessage(String clientId,
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
