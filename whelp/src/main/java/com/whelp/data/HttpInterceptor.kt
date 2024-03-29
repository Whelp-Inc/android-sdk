package com.whelp.data

import com.whelp.util.FIREBASE_TOKEN
import com.whelp.util.HASH_ID
import com.whelp.util.Preferences
import com.whelp.util.X_APP_ID
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HttpInterceptor(var preferences: Preferences) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        request.addHeader("X-HASH-VALUE", preferences.getFromPrefs(HASH_ID, "") as String)
        request.addHeader("X-APP-ID", preferences.getFromPrefs(X_APP_ID, "") as String)
        request.addHeader("X-DEVICE-OS", "android")
        request.addHeader("X-DEVICE-TOKEN", preferences.getFromPrefs(FIREBASE_TOKEN, "") as String)


        return chain.proceed(request.build())
    }
}
