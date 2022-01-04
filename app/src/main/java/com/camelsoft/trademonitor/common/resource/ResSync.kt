package com.camelsoft.trademonitor.common.resource

sealed class ResSync<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : ResSync<T>(data)
    class Error<T>(message: String, data: T? = null) : ResSync<T>(data, message)
}