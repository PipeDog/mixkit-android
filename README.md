# mixkit-android

## build.gradle 文件配置

```
// 需要自定义 module 的工程添加
apply plugin: 'mixkit-register'

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

