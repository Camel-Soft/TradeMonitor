package com.camelsoft.trademonitor.common.events

sealed class EventsSync<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : EventsSync<T>(data)
    class Error<T>(message: String, data: T? = null) : EventsSync<T>(data, message)
}