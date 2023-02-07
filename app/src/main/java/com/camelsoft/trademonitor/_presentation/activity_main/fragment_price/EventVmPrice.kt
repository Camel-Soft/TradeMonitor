package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

sealed class EventVmPrice {
    object OnAddCollClick: EventVmPrice()
    data class OnDeleteCollClick(val pos: Int): EventVmPrice()
    data class OnUpdateCollClick(val pos: Int, val newNote: String): EventVmPrice()
    data class OnShareCollClick(val pos: Int): EventVmPrice()
    object OnGetColl: EventVmPrice()
    data class OnSouthUpload(val south: String, val pos: Int): EventVmPrice()
}
