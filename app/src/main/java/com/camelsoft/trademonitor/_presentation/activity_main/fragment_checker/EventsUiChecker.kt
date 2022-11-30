package com.camelsoft.trademonitor._presentation.activity_main.fragment_checker

sealed class EventsUiChecker {
    data class ShowError(val message: String): EventsUiChecker()
    data class ShowInfo(val message: String): EventsUiChecker()
    data class ShowToast(val message: String): EventsUiChecker()
}
