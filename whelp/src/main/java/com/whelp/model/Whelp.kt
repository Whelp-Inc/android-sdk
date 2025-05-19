package com.whelp.model

import android.content.Context
import com.whelp.data.ApiService
import com.whelp.data.RetrofitClientInstance
import com.whelp.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import timber.log.Timber

data class Whelp(
    val apiKey: String?,
    val appId: String?,
    val firebaseToken: String?,
    val jsonParams: JSONObject
) {

    data class Builder(
        var apiKey: String? = null,
        var appId: String? = null,
        var firebaseToken: String? = null,
        var credentials: JSONObject? = null
    ) {

        fun key(apiKey: String) = apply { this.apiKey = apiKey }
        fun appID(appId: String) = apply { this.appId = appId }
        fun firebaseToken(firebaseToken: String) = apply { this.firebaseToken = firebaseToken }
        fun userCredentials(jsonParams: JSONObject) =
            apply { this.credentials = jsonParams }

        fun open(context: Context, scope: CoroutineScope, sdkUrl: (String) -> Unit) {
            val preferences = Preferences(context)

            firebaseToken?.let {
                preferences.saveToPrefs(FIREBASE_TOKEN, it)
            }

            val api: ApiService = RetrofitClientInstance.getRetrofitInstance(context)!!.create(
                ApiService::class.java
            )

            createHmac(context, credentials.toString())

            val res = credentials.toString().toRequestBody("application/json".toMediaType())


            scope.launch {
                try {
                    val response = api.auth(res)
                    val body = response.body()
                    val code = response.code()

                    if (body != null && code == 200) {
                        sdkUrl.invoke(body.url)
                    }
                } catch (e: Exception) {
                    Timber.tag("Whelp").d("Auth failed with: ${e.message}")
                }
            }
        }


        private fun createHmac(context: Context, json: String?) {
            val preferences = Preferences(context)
            val hashValue = Utils.hmac(json, "$apiKey$appId")

            preferences.saveToPrefs(HASH_ID, hashValue)
            preferences.saveToPrefs(X_APP_ID, appId as String)
        }
    }
}
