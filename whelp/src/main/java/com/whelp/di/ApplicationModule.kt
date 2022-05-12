package com.whelp.di

import com.whelp.data.ApiService
import com.whelp.BuildConfig
import com.whelp.data.HttpInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    private val certificatePinner = CertificatePinner.Builder()
        .add(
            "www.whelp.co",
            "sha256/yQ51W9yWI9SA6mNUp2MFiMCECsB9Sj1ZWcTuI/Joh+o="
        ).build()

    @Provides
    fun provideBaseUrl() = "https://widget-api.getwhelp.com/sdk/"


    @Provides
    @Singleton
    fun provideOkHttpClient(okHttpClient: HttpInterceptor) = if (BuildConfig.DEBUG) {
        val interceptor = HttpLoggingInterceptor()

        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(okHttpClient)
            .addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
        builder.build()
    } else {
        val interceptor = HttpLoggingInterceptor()

        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient
            .Builder()
            .addInterceptor(okHttpClient)
            .addInterceptor(interceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

}