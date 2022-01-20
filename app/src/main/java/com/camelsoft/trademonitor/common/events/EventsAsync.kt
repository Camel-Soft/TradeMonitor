package com.camelsoft.trademonitor.common.events

sealed class EventsAsync<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : EventsAsync<T>(data)
    class Error<T>(message: String, data: T? = null) : EventsAsync<T>(data, message)
    class Loading<T>(data: T? = null) : EventsAsync<T>(data)
}