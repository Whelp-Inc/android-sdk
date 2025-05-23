package com.whelp.data

import android.content.Context
import com.whelp.util.Preferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object RetrofitClientInstance {
    private lateinit var preferences: Preferences
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://widget-api.getwhelp.com/sdk/"

    fun getRetrofitInstance(context: Context): Retrofit? {
        preferences = Preferences(context)

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpInterceptor(preferences))
            .addInterceptor(interceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }
        return retrofit
    }
}