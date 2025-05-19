package com.whelp.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val url: String
)