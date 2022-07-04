package com.whelp.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
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

    fun loadWebView(token: String) {
        this.loadUrl(token)

        this.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                val errorMessage = "Error! : $error"
                showToast(errorMessage)
//                handleLoading(false)
                super.onReceivedError(view, request, error)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}