package com.whelp

import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.whelp.databinding.ActivityMainBinding
import com.whelp.model.Whelp
import org.json.JSONObject
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

        val credentials = JSONObject()
        credentials.put("language","en")

        val contact = JSONObject()
        contact.put("email","user@test.com")
        contact.put("fullname","name Surname")
        contact.put("phone","+994XXXXXXXXX")
        credentials.put("contact",contact)


        Whelp.Builder()
            .key("api_key")
            .appID("app_id")
            .userCredentials(credentials)
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