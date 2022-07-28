package com.whelp.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import android.widget.Toast
import com.whelp.R
import com.whelp.util.Utils.isChromeInstalledAndVersionGreaterThan80

class WhelpWebView : WebView {

    constructor(context: Context) : super(context) {
        initWebView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initWebView(context)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(context: Context) {

        if (context.isChromeInstalledAndVersionGreaterThan80()) {
            this.isVerticalScrollBarEnabled = true
            this.isHorizontalScrollBarEnabled = true
            this.settings.loadWithOverviewMode = true
            this.settings.useWideViewPort = true
            this.settings.javaScriptEnabled = true

            val settings = this.settings
            settings.domStorageEnabled = true
        } else {
            showToast(context.getString(R.string.chrome_warning))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}