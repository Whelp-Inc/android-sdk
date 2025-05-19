package com.whelp.util

import android.content.Context
import com.whelp.data.ApiService
import com.whelp.data.RetrofitClientInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object LogoutWhelp {
    fun clearFirebaseWhelpToken(context: Context) {
        val api = RetrofitClientInstance.getRetrofitInstance(context)!!.create(
            ApiService::class.java
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.logout()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
