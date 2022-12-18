package com.camelsoft.trademonitor.common.events

sealed class EventsProgress<T> {
    class Success<T>(val data: T): EventsProgress<T>()
    class UnSuccess<T>(val message: String): EventsProgress<T>()
    class Error<T>(val message: String): EventsProgress<T>()
    class Progress<T>(val percent: Int): EventsProgress<T>()
}
