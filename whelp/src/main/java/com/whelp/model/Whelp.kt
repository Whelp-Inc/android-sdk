package com.whelp.model

import android.content.Context
import com.whelp.data.ApiService
import com.whelp.data.RetrofitClientInstance
import com.whelp.util.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

            val call: Call<AuthResponse> = api.auth(res)


            call.enqueue(object : Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {

                    val body = response.body()
                    val code = response.code()

                    if (body != null && code == 200) {
                        sdkUrl.invoke(body.url)
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }


        private fun createHmac(context: Context, json: String?) {
            val preferences = Preferences(context)
            val hashValue = Utils.hmac(json, "$api_key$app_id")

            preferences.saveToPrefs(HASH_ID, hashValue as String)
            preferences.saveToPrefs(X_APP_ID, app_id as String)
        }
    }
}
