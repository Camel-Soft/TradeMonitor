package com.camelsoft.trademonitor._domain.use_cases.use_cases_net

sealed class EventsNet<T> {
    class Success<T>(val data: T) : EventsNet<T>()
    class Info<T>(val message: String) : EventsNet<T>()
    class Error<T>(val message: String) : EventsNet<T>()
}
