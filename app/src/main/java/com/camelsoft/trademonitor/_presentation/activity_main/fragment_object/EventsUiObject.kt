package com.camelsoft.trademonitor._presentation.activity_main.fragment_object

import com.camelsoft.trademonitor._presentation.models.MAddress

sealed class EventsUiObject {
    data class ShowError(val message: String): EventsUiObject()
    data class ShowInfo(val message: String): EventsUiObject()
    data class ShowToast(val message: String): EventsUiObject()
    data class Progress(val show: Boolean): EventsUiObject()
    data class PublicCurrent(val mAddress: MAddress): EventsUiObject()
}
