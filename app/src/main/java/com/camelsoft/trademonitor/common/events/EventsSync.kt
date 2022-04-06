package com.camelsoft.trademonitor.common.events

sealed class EventsSync<T> {
    class Success<T>(val data: T) : EventsSync<T>()
    class Error<T>(val message: String) : EventsSync<T>()
}