package com.whelp.util

import com.whelp.model.Whelp

object CredentialHelper {
    val credential = SingleLiveEvent<Whelp.Builder>()
}