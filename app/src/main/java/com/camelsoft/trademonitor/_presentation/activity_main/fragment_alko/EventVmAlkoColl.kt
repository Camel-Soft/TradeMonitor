package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko

import com.camelsoft.trademonitor._presentation.models.MChZnXmlHead

sealed class EventVmAlkoColl {
    object OnAddCollClick: EventVmAlkoColl()
    data class OnDeleteCollClick(val pos: Int): EventVmAlkoColl()
    data class OnUpdateCollClick(val pos: Int, val newNote: String): EventVmAlkoColl()
    data class OnShareCollClick(val pos: Int): EventVmAlkoColl()
    object OnGetColl: EventVmAlkoColl()
    data class OnShareChZn(val pos: Int, val mChZnXmlHead: MChZnXmlHead): EventVmAlkoColl()
}