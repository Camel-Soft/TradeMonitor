package com.camelsoft.trademonitor._presentation.activity_main.fragment_object

import com.camelsoft.trademonitor._presentation.models.MAddress

sealed class EventsVmObject {
    object RefreshAddressList : EventsVmObject()
    data class ApplyAddress(val mAddress: MAddress): EventsVmObject()
}
