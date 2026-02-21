# 📱 Practical Tutorial for Developing a Web App

This tutorial will guide you from scratch to set up a development environment on Manjaro and write a smooth "wrapper" app. We will use **Android Studio** with the native **WebView** component.

## 🛠️ Step 1: Environment Setup (Manjaro Specifics)

On Manjaro, it's highly efficient to use `pacman` or `yay` to install development tools.

1.  **Install Java Development Kit (JDK):**
    JDK 17 is currently recommended for Android development.
    ```bash
    sudo pacman -S jdk17-openjdk
    ```

    > 💡 **Tip:** Verify with `java -version`. If it's not 17, execute:
    > `sudo archlinux-java set java-17-openjdk`

2.  **Install Android Studio:**
    The easiest way is to install it via AUR, which automatically configures the environment path:
    ```bash
    yay -S android-studio
    ```

3.  **Initial Configuration:**
    Launch Android Studio and follow the Setup Wizard to download the **SDK Platform** and **SDK Build-Tools**.

---

## 🏗️ Step 2: Create a New Project

1.  Click **New Project**.
2.  Select **Empty Views Activity** (Note: not Compose, as WebView is more stable with traditional Views).
3.  **Project Parameter Settings:**
    *   **Name:** `VeryGoodLMArena`
    *   **Language:** **Java**
    *   **Minimum SDK:** API 24 (Android 7.0) — for compatibility with mainstream devices.

4.  Click **Finish** and wait for the Gradle build to complete (it might be a bit slow the first time, so grab a coffee ☕).

---

## 🔑 Step 3: Configure Permissions and Security (AndroidManifest.xml)

An app needs a "pass" to access the internet. Find `app > manifests > AndroidManifest.xml`.

1.  Add the following **above** the `<application>` tag:
    ```xml
    <uses-permission android:name="android.permission.INTERNET" />
    ```

2.  **Allow loading HTTP pages** (optional): If you encounter a blank screen when loading some pages, add the following inside the `<application>` tag:
    ```xml
    android:usesCleartextTraffic="true"
    ```

---

## 🎨 Step 4: UI Layout (activity_main.xml)

Find `app > res > layout > activity_main.xml`. We're going to give our WebView a "full-screen stage".

**Replace the code with:**

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

> 🧩 **Tip:** Click the **Code** button in the top right corner to switch to pure code mode.

---

## 🧠 Step 5: Core Logic (MainActivity.java)

This is the "brain" of the app. Find `app > java > [your package name] > MainActivity.java`.

**Complete optimized code:**

```java
package com.deepjhshenjing.verygoodlmarena;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.OnBackPressedCallback; // Modern back button handling
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView lmarenaWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lmarenaWebView = findViewById(R.id.lmarena_webview);
        
        // --- Minimal optimized WebView configuration ---
        WebSettings settings = lmarenaWebView.getSettings();
        settings.setJavaScriptEnabled(true);    // Enable JavaScript
        settings.setDomStorageEnabled(true);     // Enable DOM Storage (to prevent login sessions from expiring)
        settings.setDatabaseEnabled(true);
        settings.setUserAgentString(settings.getUserAgentString() + " VGLMA_App"); // Identify your App

        // Core: Open web pages within the app, don't jump to the browser
        lmarenaWebView.setWebViewClient(new WebViewClient());

        // Load the target URL
        lmarenaWebView.loadUrl("https://arena.ai?mode=direct");

        // --- Modern back button handling ---
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (lmarenaWebView.canGoBack()) {
                    lmarenaWebView.goBack(); // Go back in web history
                } else {
                    finish(); // Exit the application
                }
            }
        });
    }
}
```

> ⚠️ **Correction Note:** The original tutorial's `onBackPressed()` is now officially deprecated. Using `OnBackPressedDispatcher` prevents issues on Android 13+ devices.

---

## 🎨 Step 6: Customize Appearance (Icon and Name)

### 1. Generate Icons Automatically

1.  Right-click the `app` folder -> **New > Image Asset**.
2.  **Path:** Select your logo image.
3.  **Resize:** Adjust the slider to ensure the logo is within the circle.
4.  Click **Finish**. Android Studio will handle all the resolutions for you!

### 2. Change the Application Name

1.  Open `app > res > values > strings.xml`.
2.  Change the value of `app_name`:
    ```xml
    <string name="app_name">Large Model Arena</string>
    ```

---

## 🚀 Step 7: Compile and Export

### 1. Live Debugging

*   Enable **USB Debugging** on your phone.
*   Click the small green **Run** arrow 🏃 at the top of Android Studio.

### 2. Generate an Installation Package (APK)

*   Click **Build > Build Bundle(s) / APK(s) > Build APK(s)** in the menu bar.
*   After it's finished, click **Locate** in the popup in the bottom-right corner.
*   Send the generated `app-debug.apk` to your phone and install it!
