package com.regimtv.launcher

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(9, 13, 24)
        window.navigationBarColor = android.graphics.Color.rgb(9, 13, 24)

        webView = WebView(this)
        webView.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        webView.setBackgroundColor(android.graphics.Color.rgb(9, 13, 24))
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadsImagesAutomatically = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.addJavascriptInterface(LauncherBridge(), "RegimNative")
        setContentView(webView)
        webView.loadUrl("https://regisjunior344-jpg.github.io/mycinema-tv/")
    }


    override fun dispatchKeyEvent(event: android.view.KeyEvent): Boolean {
        if (event.action == android.view.KeyEvent.ACTION_DOWN) {
            val key = when (event.keyCode) {
                android.view.KeyEvent.KEYCODE_DPAD_UP -> "ArrowUp"
                android.view.KeyEvent.KEYCODE_DPAD_DOWN -> "ArrowDown"
                android.view.KeyEvent.KEYCODE_DPAD_LEFT -> "ArrowLeft"
                android.view.KeyEvent.KEYCODE_DPAD_RIGHT -> "ArrowRight"
                android.view.KeyEvent.KEYCODE_DPAD_CENTER,
                android.view.KeyEvent.KEYCODE_ENTER -> "Enter"
                else -> null
            }
            if (key != null && ::webView.isInitialized) {
                webView.evaluateJavascript("window.regimHandleRemoteKey && window.regimHandleRemoteKey('$key')", null)
                return true
            }
        }
        return super.dispatchKeyEvent(event)
    }

    inner class LauncherBridge {
        @JavascriptInterface
        fun openPlayStore() {
            runOnUiThread {
                val intent = packageManager.getLaunchIntentForPackage("com.android.vending")
                if (intent != null) startActivity(intent)
                else Toast.makeText(this@MainActivity, "Google Play Store não encontrada", Toast.LENGTH_SHORT).show()
            }
        }

        @JavascriptInterface
        fun launchApp(packageName: String) {
            runOnUiThread {
                val intent = packageManager.getLaunchIntentForPackage(packageName)
                if (intent != null) startActivity(intent)
                else Toast.makeText(this@MainActivity, "Aplicativo não encontrado", Toast.LENGTH_SHORT).show()
            }
        }

        @JavascriptInterface
        fun openExternal(url: String) {
            runOnUiThread {
                if (url.startsWith("https://")) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
        }
    }

    @Deprecated("Deprecated in Android API")
    override fun onBackPressed() {
        // Keep the TV home surface in place instead of leaving the launcher.
    }
}
