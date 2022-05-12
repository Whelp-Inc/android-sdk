package com.whelp.data

import com.whelp.model.UserCredentials
import javax.inject.Inject

class GetUrlUseCase @Inject constructor(val repository: AuthRepository) {
    suspend fun execute(userCredentials: UserCredentials) = repository.auth(userCredentials)
}