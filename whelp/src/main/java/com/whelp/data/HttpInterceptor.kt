package com.whelp.data

import android.content.SharedPreferences
import com.whelp.util.HASH_ID
import com.whelp.util.X_APP_ID
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class HttpInterceptor @Inject constructor(var preferences: SharedPreferences) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        request.addHeader("X-HASH-VALUE", preferences.getString(HASH_ID, "") as String)
        request.addHeader("X-APP-ID", preferences.getString(X_APP_ID, "") as String)


        val response = chain.proceed(request.build())



        return response
    }
}