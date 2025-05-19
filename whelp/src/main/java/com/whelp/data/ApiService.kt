package com.whelp.data

import com.whelp.model.AuthResponse
import io.reactivex.Single
import io.reactivex.Completable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth")
    fun auth(@Body auth: RequestBody?): Call<AuthResponse>

    @POST("logout")
    fun logout(): Completable
}
