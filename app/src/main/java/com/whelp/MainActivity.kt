package com.whelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.whelp.databinding.ActivityMainBinding
import com.whelp.model.Contact
import com.whelp.model.UserCredentials
import com.whelp.model.Whelp
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val chromeClient = AppChromeClient(WeakReference(this))
    private var contentLauncher: ActivityResultLauncher<String> = getMultipleContentLauncher()

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userCredentials =
            UserCredentials(
                "en", Contact(
                    "user@test.com", "Name Surname",
                    "+994XXXXXXXXX"
                )
            )

        Whelp.Builder()
            .key("app_key")
            .appID("app_id")
            .userCredentials(userCredentials)
            .open(this) {
                Log.d(TAG, "onCreate: $it")

                binding.whelpView.webChromeClient = chromeClient

                binding.whelpView.loadUrl(it)
            }
    }

    fun launchGetMultipleContents(type: String) {
        contentLauncher.launch(type)
    }

    private fun getMultipleContentLauncher(): ActivityResultLauncher<String> {
        return this.registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { list ->
            chromeClient.receiveFileCallback(list.toTypedArray())
        }
    }
}