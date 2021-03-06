# mixkit-android

## build.gradle 文件配置

```
// 需要自定义 module 的工程添加
apply plugin: 'mixkit-plugin'

dependencies {
    // 工程中都需要引入
    implementation project(path: ':mixkit')
    annotationProcessor project(path: ':mixkit')

    // 以下配置按所需功能引入对应模块
    implementation project(path: ':mixkit-socket')
    implementation project(path: ':mixkit-web')
    implementation project(path: ':mixkit-messenger')

    // 需要自定义 module 的工程添加
    implementation project(path: ':mixkit-compiler')
    // 注意：如果使用了 kotlin，并且配置了 `kotlin-kapt` 插件，则需要将下面代码替换为 
    // ```
    //  kapt project(path: ':mixkit-compiler')
    // ```
    annotationProcessor project(path: ':mixkit-compiler')
}
```

## 跨进程通信示例

### 跨进程服务所在项目工程 AndroidManifest.xml 文件配置

```
// 1、此处 package 值需要做为初始化参数传入引擎
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.pipedog.mixkit.example">

    <application
        android:name="MixApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Mixkitandroid"
        android:usesCleartextTraffic="true">


        <service
            // 2、这里指定 Service 的全类名（此类处于 SDK 中，固定写死即可）
            android:name="com.pipedog.mixkit.messenger.server.MessengerService"
            android:enabled="true"
            android:exported="true"
            android:persistent="true"

            // 3、自定义服务进程名称
            android:process=":serverProcess"
            >
            <intent-filter>
                // 4、此处 action 值需要做为初始化参数传入引擎
                <action android:name="com.pipedog.testService" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </service>

    </application>

</manifest>
```

### 跨进程通信客户端请求指令发送示例

```
private void startEngine() {
    IMessengerEngine engine = MessengerEngine.getInstance();

    // 1、初始化配置
    engine.setupConfiguration(new IMessengerEngine.IConfigurationCallback() {
        @Override
        public void setup(IMessengerEngine.IInitialConfiguration configuration) {
            configuration.setContext(getApplicationContext());
            configuration.setClientId("com.client.executorApp");

            // action 及 package 需要在服务端进程的 AndroidManifest.xml 中配置
            configuration.setAction("com.pipedog.testService");
            configuration.setPackage("com.pipedog.mixkit.example");
        }
    });

    // 2、启动引擎
    engine.start();
}

private void sendMessage() {
    // 3、调用其他进程的方法并获取回调
    String clientId = "com.client.mainApp";
    String moduleName = "MessengerTestModule";
    String methodName = "testMethod";

    MixResultCallback callback = new MixResultCallback() {
        @Override
        public void invoke(Object[] response) {
            // Handle result here
        }
    };

    engine.sendMessage(clientId, moduleName, methodName,
        Arrays.asList("argument 1", "argument 2", callback));
}
```

## 自定义 Module 实现执行功能（Web、Socket 以及 Messenger 等可以通用）

```
// 1、注解自定义 Module 名称
@MixModule(name = "MessengerTestModule")
public class MessengerTestModule {

    // 2、注解自定义 Method 名称
    @MixMethod(name = "testMethod")
    public void testMethod(String str, 
        Integer i, int bi, MixResultCallback callback, 
        Map<String, Object> m, List<Object> list) {
            
        // 3、处理业务逻辑
        // ...

        // 4. 结果回调（回调结果为数组类型，但一般只存放一个元素）
        List<Object> response = new ArrayList<>();
        callback.invoke(response.toArray());
    }

}
```

### 跨进程通信自定义服务端监听

创建实现了 `com.pipedog.mixkit.messenger.interfaces.IServerListener` 接口的类，并重写相关方法即可，不需要对该类进行任何额外操作

```
public class TestServerListener implements IServerListener {

    @Override
    public void didFailSendMessage2SourceClient(ErrorMessage errorMessage) {
        // Do something here...
    }

    @Override
    public void didFailSendMessage2TargetClient(ErrorMessage errorMessage) {
        // Do something here...
    }

}
```

### 跨进程通信自定义客户端监听

实现 `com.pipedog.mixkit.messenger.interfaces.IClientListener` 接口，然后进行注入即可

```
1、创建类
public class TestClientListener implements IClientListener {
    // 按需重写方法
}

2、监听绑定
ClientListenerManager.getInstance().bindListener(this);
```

### 跨进程通信消息校验（支持服务端、客户端双端校验）

创建实现了 `com.pipedog.mixkit.messenger.interfaces.IMessageVerifier` 接口的类，并重写其方法：

```
public class TestMessageVerifier implements IMessageVerifier {

    @Override
    public int getVerifierType() {
        // 这里同时用于服务端和客户端双端验证，如果单独支持客户端或服务端，直接返回该值即可
        return IMessageVerifier.VERIFIER_TYPE_SERVER | IMessageVerifier.VERIFIER_TYPE_CLIENT;
    }

    @Override
    public boolean isValidMessage(Message message) {
        // 在这里对 message 进行校验，如果不符合规则或者来源不明，则应返回 false，则此条 message 会被忽略
        return true;
    }

}
```