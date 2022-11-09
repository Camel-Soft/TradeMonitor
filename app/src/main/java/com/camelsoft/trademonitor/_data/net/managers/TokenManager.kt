package com.camelsoft.trademonitor._data.net.managers

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.camelsoft.trademonitor.common.App.Companion.getAppContext

object TokenManager {
    private val sharedPreferences = getAppContext().getSharedPreferences("TokenManager", Context.MODE_PRIVATE)

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    init {
        if (!sharedPreferences.contains("token")) _token.value = null
        else _token.value = sharedPreferences.getString("token", null)
    }

    fun putToken(token: String?) {
        synchronized(this) {
            _token.value = token
            val editor = sharedPreferences.edit()
            editor.putString("token", token)
            editor.apply()
        }
    }

    fun getToken(): String? {
        return token.value
    }
}
