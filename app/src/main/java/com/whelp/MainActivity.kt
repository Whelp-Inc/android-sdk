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
        credentials.put("language", "en")

        val contact = JSONObject()
        val identity = JSONObject()

        contact.put("email", "user1@test.com")
        contact.put("fullname", "name1 Surname1")
        contact.put("phone", "+994776434195")

        identity.put("field", "email")

        credentials.put("contact", contact)
        credentials.put("identity", identity)


        Whelp.Builder()
            .key("api_key")
            .appID("app_id")
            .firebaseToken("it")
            .userCredentials(credentials)
            .open(this) {
                Log.d(TAG, "onCreate: $it")

                binding.whelpView.webChromeClient = chromeClient

                binding.whelpView.loadUrl(it)
            }

    }

//        LogoutWhelp.clearFirebaseWhelpToken(applicationContext)


    fun launchGetMultipleContents(type: String) {
        contentLauncher.launch(type)
    }

    private fun getMultipleContentLauncher(): ActivityResultLauncher<String> {
        return this.registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { list ->
            chromeClient.receiveFileCallback(list.toTypedArray())
        }
    }
}
