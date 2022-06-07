package com.whelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.whelp.databinding.ActivityMainBinding
import com.whelp.model.Contact
import com.whelp.model.UserCredentials
import com.whelp.model.Whelp

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

        binding.button.setOnClickListener {
            Whelp.Builder()
                .key("key")
                .appID("appId")
                .userCredentials(userCredentials)
                .open(this)
        }
    }
}