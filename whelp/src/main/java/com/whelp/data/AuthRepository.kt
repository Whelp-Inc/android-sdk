package com.whelp.data

import com.whelp.model.AuthResponse
import com.whelp.model.UserCredentials
import retrofit2.http.Body

interface AuthRepository {
    suspend fun auth(@Body body: UserCredentials): AuthResponse
}