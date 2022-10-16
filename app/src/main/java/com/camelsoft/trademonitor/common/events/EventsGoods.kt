package com.camelsoft.trademonitor.common.events

sealed class EventsGoods<T> {
    class Success<T>(val data: T) : EventsGoods<T>()
    class UnSuccess<T>(val message: String) : EventsGoods<T>()
    class Update<T>(val message: String) : EventsGoods<T>()
    class Error<T>(val message: String) : EventsGoods<T>()
}
