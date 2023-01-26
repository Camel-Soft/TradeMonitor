package com.camelsoft.trademonitor.common.events

sealed class EventsOkInEr<T> {
    class Success<T>(val data: T) : EventsOkInEr<T>()
    class Info<T>(val message: String) : EventsOkInEr<T>()
    class Error<T>(val message: String) : EventsOkInEr<T>()
}
