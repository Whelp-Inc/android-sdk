package com.whelp.data

import com.whelp.model.AuthResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth")
    suspend fun auth(@Body auth: RequestBody?): Response<AuthResponse>
}
