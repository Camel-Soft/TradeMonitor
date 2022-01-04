package com.camelsoft.trademonitor.common.resource

sealed class ResAsync<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : ResAsync<T>(data)
    class Error<T>(message: String, data: T? = null) : ResAsync<T>(data, message)
    class Loading<T>(data: T? = null) : ResAsync<T>(data)
}