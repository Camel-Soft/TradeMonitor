package com.camelsoft.trademonitor._presentation.activity_main.fragment_sign

sealed class EventsUiSign {
    data class ShowError(val message: String): EventsUiSign()
    data class ShowInfo(val message: String): EventsUiSign()
    data class ShowToast(val message: String): EventsUiSign()
    data class Progress(val show: Boolean): EventsUiSign()
    object Close: EventsUiSign()
}
