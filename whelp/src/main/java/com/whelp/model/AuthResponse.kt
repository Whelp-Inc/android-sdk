package com.whelp.model

// Nullable on purpose: if the response is missing "url" (or parsing fails),
// we must not hand null to the host app's non-null (String) -> Unit callback.
data class AuthResponse(
    val url: String?
)
