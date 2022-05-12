package com.whelp.data

import com.whelp.model.AuthResponse
import com.whelp.model.UserCredentials
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth")
    suspend fun auth(@Body auth: UserCredentials) : AuthResponse

}