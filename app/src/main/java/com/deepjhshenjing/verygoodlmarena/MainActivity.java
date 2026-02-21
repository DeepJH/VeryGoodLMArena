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