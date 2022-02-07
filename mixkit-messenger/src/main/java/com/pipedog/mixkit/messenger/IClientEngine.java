package com.pipedog.mixkit.messenger;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * 跨进程通信客户端引擎接口
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
 *          android:exported="true"
 *          android:persistent="true"
 *          android:process=":customServerProcessName"
 *          >
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
public interface IClientEngine {

    // PUBLIC INTERFACES

    public interface IInitialConfiguration {

        /**
         * 设置 app 上下文
         */
        void setContext(@NonNull Context context);

        /**
         * 设置当前客户端 ID 标识（允许自定义）
         */
        void setClientId(@NonNull String clientId);

        /**
         * 设置 Action（在服务端进程的 AndroidManifest.xml 文件中配置的 action 值）
         */
        void setAction(@NonNull String action);

        /**
         * 设置服务进程包名（在服务端进程 AndroidManifest.xml 文件中配置的包名）
         */
        void setPackage(@NonNull String packageName);

    }

    public interface IConfigurationCallback {
        void setup(@NonNull IInitialConfiguration configuration);
    }


    // PUBLIC METHODS

    /**
     * 初始化配置
     * @param callback 配置回调
     */
    public void setupConfiguration(@NonNull IConfigurationCallback callback);

    /**
     * 启动跨进程消息引擎
     * @return true 启动成功，false 启动失败
     */
    public boolean start();

    /**
     * 重启跨进程消息引擎
     * @return true 启动成功，false 启动失败
     */
    public boolean restart();

    /**
     * 关闭跨进程消息引擎
     */
    public void shutdown();

    /**
     * 发送执行消息到指定客户端
     *
     * @param clientId 目标客户端 ID
     * @param moduleName 模块名称
     * @param methodName 方法名
     * @param arguments 参数列表（包含回调，回调类型为 com.pipedog.mixkit.kernel.MixResultCallback），
     *                  （1）你可以同时在参数列表中传递多个回调，但要注意，你最多只会收到一个回调；
     *                  （2）一旦因为进程之间的连接出现问题，或者目标（执行）客户端执行功能失败，你会在
     *                      ClientListener 中收到失败的回调，这时参数列表中的所有回调都会失效，不会再响应；
     *                  （3）你可以通过 ClientListenerManager 中进行 ClientListener 的注册；
     * @return 本次请求的 traceId（可以做为唯一标识来进行使用）
     */
    public @NonNull String sendMessage(@NonNull String clientId,
                                       @NonNull String moduleName,
                                       @NonNull String methodName,
                                       @NonNull List<Object> arguments);

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
