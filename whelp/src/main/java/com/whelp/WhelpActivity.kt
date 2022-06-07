package com.whelp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.whelp.databinding.ActivityWhelpBinding
import com.whelp.model.UserCredentials
import com.whelp.util.CredentialHelper
import com.whelp.util.HASH_ID
import com.whelp.util.Preferences
import com.whelp.util.Utils.hmac
import com.whelp.util.Utils.isChromeInstalledAndVersionGreaterThan80
import com.whelp.util.Utils.isOnline
import com.whelp.util.X_APP_ID
import java.lang.ref.WeakReference

class WhelpActivity : AppCompatActivity() {
    private lateinit var preferences: Preferences
    private lateinit var binding: ActivityWhelpBinding
    private lateinit var viewModel: WhelpViewModel
    private val chromeClient = AppChromeClient(WeakReference(this))
    private var contentLauncher: ActivityResultLauncher<String> = getMultipleContentLauncher()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[WhelpViewModel::class.java]
        preferences = Preferences(applicationContext)

        if (this.isChromeInstalledAndVersionGreaterThan80()) {
            initWebView()

            if (!this.isOnline()) {
                showMessage(getString(R.string.no_internet))

                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                return
            }


            viewModel.url.observe(this) {
                if (isOnline())
                    loadWebView(it)
            }

            viewModel.loadingState.observe(this) {
                handleLoading(it)
            }

            viewModel.sdkErrorState.observe(this) {
                finish()
            }

            CredentialHelper.credential.observe(this) {
                val moshi = Moshi.Builder().build()
                val jsonAdapter: JsonAdapter<UserCredentials> =
                    moshi.adapter(UserCredentials::class.java)

                val json = jsonAdapter.toJson(it.userCredentials)

                val hashValue = hmac(json.toString(), "${it.api_key}${it.app_id}")

                preferences.saveToPrefs(HASH_ID, hashValue as String)
                preferences.saveToPrefs(X_APP_ID, it.app_id as String)

                viewModel.getUrl(applicationContext, it.userCredentials!!)
            }
        } else {
            binding.chromeWarning.visibility = View.VISIBLE
            binding.webView.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun initWebView() {
        binding.apply {
            webView.isVerticalScrollBarEnabled = true
            webView.isHorizontalScrollBarEnabled = true
            webView.settings.loadWithOverviewMode = true
            webView.settings.useWideViewPort = true
            webView.settings.javaScriptEnabled = true

            val settings = webView.settings
            settings.domStorageEnabled = true
            webView.webChromeClient = chromeClient
        }
    }

    private fun getMultipleContentLauncher(): ActivityResultLauncher<String> {
        return this.registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { list ->
            if (list.isEmpty()) {
                showToast("No files selected")
            }
            chromeClient.receiveFileCallback(list.toTypedArray())
        }
    }


    fun launchGetMultipleContents(type: String) {
        contentLauncher.launch(type)
    }

    private fun loadWebView(token: String) {
        binding.webView.loadUrl(token)

        binding.webView.webViewClient = object : WebViewClient() {
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
                handleLoading(false)
                super.onReceivedError(view, request, error)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleLoading(loadingState: Boolean) {
        if (loadingState) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


    private fun showMessage(message: String) {
        val parentLayout = findViewById<View>(android.R.id.content)
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }
}