package com.whelp.util

import android.content.Context
import com.whelp.data.ApiService
import com.whelp.data.RetrofitClientInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object LogoutWhelp {
    fun clearFirebaseWhelpToken(context: Context) {
        val api = RetrofitClientInstance.getRetrofitInstance(context)!!.create(
            ApiService::class.java
        )

        api.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // Success
            }, { error ->
                error.printStackTrace()
            })
    }
}
