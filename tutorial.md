# 📱 网页 App 开发实战教程

本教程将带你从零开始，在 Manjaro 环境下搭建开发环境，并编写一个丝滑的“套壳” App。我们将使用 **Android Studio** 配合原生 **WebView** 组件。

## 🛠️ 第一步：环境搭建 (Manjaro 特色)

Manjaro 建议使用 `pacman` 或 `yay` 来安装开发工具，效率极高！

1. **安装 Java 开发工具包 (JDK):**
   安卓开发目前推荐 JDK 17。
```bash
sudo pacman -S jdk17-openjdk

```


> 💡 **小贴士：** 使用 `java -version` 验证。如果不是 17，请执行：
> `sudo archlinux-java set java-17-openjdk`


2. **安装 Android Studio:**
   通过 AUR 安装是最省心的，它会自动配置好环境路径：
```bash
yay -S android-studio

```


3. **初始化配置:**
   启动 Android Studio，跟随 Setup Wizard 下载 **SDK Platform** 和 **SDK Build-Tools**。

---

## 🏗️ 第二步：创建新项目

1. 点击 **New Project**。
2. 选择 **Empty Views Activity** (注意：不是 Compose，WebView 在传统 Views 下更稳定)。
3. **项目参数设置:**
* **Name:** `VeryGoodLMArena`
* **Language:** **Java**
* **Minimum SDK:** API 24 (Android 7.0) —— 兼顾主流设备。


4. 点击 **Finish**，静候 Gradle 构建完成（第一次可能有点慢，喝杯咖啡 ☕）。

---

## 🔑 第三步：配置权限与安全 (AndroidManifest.xml)

App 联网需要“通行证”。找到 `app > manifests > AndroidManifest.xml`。

1. 在 `<application>` 标签**上方**添加：
```xml
<uses-permission android:name="android.permission.INTERNET" />

```


2. **允许加载 HTTP 网页**（可选）：如果在加载某些页面时白屏，请在 `<application>` 标签内添加：
```xml
android:usesCleartextTraffic="true"

```



---

## 🎨 第四步：界面布局 (activity_main.xml)

找到 `app > res > layout > activity_main.xml`，我们要给 WebView 一个“全屏舞台”。

**将代码替换为：**

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <WebView
        android:id="@+id/lmarena_webview"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</FrameLayout>

```

> 🧩 **技巧：** 点击右上角的 **Code** 按钮即可切换到纯代码模式。

---

## 🧠 第五步：核心逻辑 (MainActivity.java)

这是 App 的“大脑”。找到 `app > java > [你的包名] > MainActivity.java`。

**优化后的完整代码：**

```java
package com.deepjhshenjing.verygoodlmarena;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.OnBackPressedCallback; // 现代化的返回键处理
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView lmarenaWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lmarenaWebView = findViewById(R.id.lmarena_webview);
        
        // --- WebView 极简优化配置 ---
        WebSettings settings = lmarenaWebView.getSettings();
        settings.setJavaScriptEnabled(true);    // 开启 JS
        settings.setDomStorageEnabled(true);     // 开启 DOM 存储（防止登录失效）
        settings.setDatabaseEnabled(true);
        settings.setUserAgentString(settings.getUserAgentString() + " VGLMA_App"); // 标识你的 App

        // 核心：留在 App 内打开网页，不跳浏览器
        lmarenaWebView.setWebViewClient(new WebViewClient());

        // 加载目标
        lmarenaWebView.loadUrl("https://arena.ai?mode=direct");

        // --- 现代化返回键处理 ---
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (lmarenaWebView.canGoBack()) {
                    lmarenaWebView.goBack(); // 网页后退
                } else {
                    finish(); // 退出应用
                }
            }
        });
    }
}

```

> ⚠️ **修正说明：** 原教程中的 `onBackPressed()` 已被官方标记为弃用，使用 `OnBackPressedDispatcher` 能避免在 Android 13+ 设备上失效。

---

## 🎨 第六步：定制颜值 (图标与名称)

### 1. 自动生成图标

1. 右键点击 `app` 文件夹 -> **New > Image Asset**。
2. **Path:** 选择你的 Logo 图片。
3. **Resize:** 调整滑块确保 Logo 在圆圈内。
4. 点击 **Finish**。Android Studio 会帮你搞定所有分辨率！

### 2. 修改应用名称

1. 打开 `app > res > values > strings.xml`。
2. 修改 `app_name` 的值：
```xml
<string name="app_name">大模型竞技场</string>

```



---

## 🚀 第七步：编译与导出

### 1. 即时调试

* 手机开启 **USB 调试**。
* 点击 Android Studio 顶部的绿色小箭头 **Run** 🏃。

### 2. 生成安装包 (APK)

* 点击菜单栏 **Build > Build Bundle(s) / APK(s) > Build APK(s)**。
* 完成后，点击右下角弹窗的 **Locate**。
* 把生成的 `app-debug.apk` 发送到手机安装即可！