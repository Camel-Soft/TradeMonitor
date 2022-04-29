package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko

sealed class EventVmAlkoColl {
    object OnAddCollClick: EventVmAlkoColl()
    data class OnDeleteCollClick(val pos: Int): EventVmAlkoColl()
    data class OnUpdateCollClick(val pos: Int, val newNote: String): EventVmAlkoColl()
    data class OnShareCollClick(val pos: Int): EventVmAlkoColl()
    object OnGetColl: EventVmAlkoColl()
}