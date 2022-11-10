package com.camelsoft.trademonitor._domain.use_cases.use_cases_repository

sealed class EventsGoods<T> {
    class Success<T>(val data: T) : EventsGoods<T>()
    class UnSuccess<T>(val message: String) : EventsGoods<T>()
    class Update<T>(val message: String) : EventsGoods<T>()
    class Error<T>(val message: String) : EventsGoods<T>()
}
