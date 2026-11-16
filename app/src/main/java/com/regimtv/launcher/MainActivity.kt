package com.regimtv.launcher

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.KeyEvent
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : Activity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(9, 13, 24)
        window.navigationBarColor = android.graphics.Color.rgb(9, 13, 24)
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        )

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
        webView.isFocusable = true
        webView.isFocusableInTouchMode = true
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
                KeyEvent.KEYCODE_DPAD_CENTER,
                KeyEvent.KEYCODE_ENTER -> "Enter"
                KeyEvent.KEYCODE_BACK,
                KeyEvent.KEYCODE_ESCAPE -> "Escape"
                else -> null
            }
            if (key != null && ::webView.isInitialized) {
                webView.evaluateJavascript("window.regimHandleRemoteKey ? window.regimHandleRemoteKey('$key') : false", null)
                return true
            }
        }
        return super.dispatchKeyEvent(event)
    }

    inner class LauncherBridge {
        @JavascriptInterface
        fun getInstalledApps(): String {
            val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
            val resolved = packageManager.queryIntentActivities(intent, 0)
            val seen = mutableSetOf<String>()
            val apps = JSONArray()
            resolved.map { it.activityInfo }.sortedBy { it.loadLabel(packageManager).toString().lowercase() }.forEach { info ->
                val pkg = info.packageName
                if (pkg != packageName && seen.add(pkg)) {
                    val item = JSONObject()
                    item.put("name", info.loadLabel(packageManager).toString())
                    item.put("packageName", pkg)
                    apps.put(item)
                }
            }
            return apps.toString()
        }

        @JavascriptInterface
        fun openShieldSettings() {
            runOnUiThread {
                try {
                    startActivity(Intent(Settings.ACTION_SETTINGS))
                } catch (_: Exception) {
                    Toast.makeText(this@MainActivity, "Não foi possível abrir as configurações do Shield", Toast.LENGTH_SHORT).show()
                }
            }
        }

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
