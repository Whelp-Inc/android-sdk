package com.whelp.model

import android.content.Context
import android.content.Intent
import com.whelp.WhelpActivity
import com.whelp.util.CredentialHelper

data class Whelp private constructor(
    val api_key: String?,
    val app_id: String?,
    val userCredentials: UserCredentials?
) {

    data class Builder(
        var api_key: String? = null,
        var app_id: String? = null,
        var userCredentials: UserCredentials? = null
    ) {

        fun key(api_key: String) = apply { this.api_key = api_key }
        fun appID(app_id: String) = apply { this.app_id = app_id }
        fun userCredentials(userCredentials: UserCredentials) =
            apply { this.userCredentials = userCredentials }

        fun open(context: Context) {
            val intent = Intent(context.applicationContext, WhelpActivity::class.java)
            CredentialHelper.credential.value = Builder(api_key, app_id, userCredentials)
            context.startActivity(intent)
        }

    }
}