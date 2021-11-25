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

## 跨进程通信配置示例
### 服务所在项目工程 AndroidManifest.xml 文件配置

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

