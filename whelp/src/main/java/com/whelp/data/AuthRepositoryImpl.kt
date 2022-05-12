package com.whelp.data

import com.whelp.model.AuthResponse
import com.whelp.model.UserCredentials
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val apiService: ApiService) : AuthRepository {
    override suspend fun auth(body: UserCredentials): AuthResponse {
        return apiService.auth(body)
    }

}