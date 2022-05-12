package com.whelp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whelp.data.GetUrlUseCase
import com.whelp.model.UserCredentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WhelpViewModel @Inject constructor(private val useCase: GetUrlUseCase) : ViewModel() {

    val url = MutableLiveData<String>()
    fun getUrl(userCredentials: UserCredentials) {

        viewModelScope.launch {
            flow {
                emit(useCase.execute(userCredentials))
            }.flowOn(Dispatchers.IO)
                .catch {
                    it.printStackTrace()
                }
                .collect {
                    url.value = it.url
                    Log.d("getUrl", "getUrl: ${it.url}")
                }
        }
    }

}