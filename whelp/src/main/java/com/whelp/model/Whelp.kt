package com.whelp.model

import android.content.Context
import com.whelp.data.ApiService
import com.whelp.data.RetrofitClientInstance
import com.whelp.util.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Whelp constructor(
    val api_key: String?,
    val app_id: String?,
    val firebaseToken: String?,
    val jsonParams: JSONObject
) {

    data class Builder(
        var api_key: String? = null,
        var app_id: String? = null,
        var firebaseToken: String? = null,
        var jsonParams: JSONObject? = null
    ) {

        fun key(api_key: String) = apply { this.api_key = api_key }
        fun appID(app_id: String) = apply { this.app_id = app_id }
        fun firebaseToken(firebaseToken: String) = apply { this.firebaseToken = firebaseToken }
        fun userCredentials(jsonParams: JSONObject) =
            apply { this.jsonParams = jsonParams }

        fun open(context: Context, sdkUrl: (String) -> Unit) {
            val preferences = Preferences(context)

            firebaseToken?.let {
                preferences.saveToPrefs(FIREBASE_TOKEN, it)
            }

            val api: ApiService = RetrofitClientInstance.getRetrofitInstance(context)!!.create(
                ApiService::class.java
            )

            createHmac(context, jsonParams.toString())

            val res = jsonParams.toString().toRequestBody("application/json".toMediaType())

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val url = api.auth(res).url
                    if (!url.isNullOrBlank()) {
                        sdkUrl.invoke(url)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


        private fun createHmac(context: Context, json: String?) {
            val preferences = Preferences(context)
            val hashValue = Utils.hmac(json, "$api_key$app_id")

            preferences.saveToPrefs(HASH_ID, hashValue as String)
            preferences.saveToPrefs(X_APP_ID, app_id as String)
        }
    }
}
