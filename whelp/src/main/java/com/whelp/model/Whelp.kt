package com.whelp.model

import android.content.Context
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.whelp.data.ApiService
import com.whelp.data.RetrofitClientInstance
import com.whelp.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class Whelp constructor(
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

        fun open(context: Context, sdkUrl: (String) -> Unit) {

//            val intent = Intent(context.applicationContext, WhelpActivity::class.java)
//            CredentialHelper.credential.value = Builder(api_key, app_id, userCredentials)
//            context.startActivity(intent)

            createHmac(context)

            val api: ApiService = RetrofitClientInstance.getRetrofitInstance(context)!!.create(
                ApiService::class.java
            )
            val call: Call<AuthResponse> = api.auth(userCredentials!!)

            call.enqueue(object : Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>?,
                    response: Response<AuthResponse>
                ) {
//                    loadingState.value = false

                    val body = response.body()
                    val code = response.code()
                    if (body != null && code == 200) {
                        sdkUrl.invoke(body.url)
                    } else {
//                        sdkErrorState.value = true
                    }
                }

                override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                    Log.d("onFailure", "onFailure: ${t?.printStackTrace()}")
//                    loadingState.value = false
                }
            })
        }

        private fun createHmac(context: Context) {
            val preferences = Preferences(context)

            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<UserCredentials> =
                moshi.adapter(UserCredentials::class.java)

            val json = jsonAdapter.toJson(userCredentials)

            val hashValue = Utils.hmac(json.toString(), "$api_key$app_id")

            preferences.saveToPrefs(HASH_ID, hashValue as String)
            preferences.saveToPrefs(X_APP_ID, app_id as String)
        }
    }
}