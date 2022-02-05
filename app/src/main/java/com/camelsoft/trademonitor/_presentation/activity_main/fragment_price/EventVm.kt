package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

sealed class EventVm {
    object OnAddCollClick: EventVm()
    data class OnDeleteCollClick(val pos: Int): EventVm()
    data class OnUpdateCollClick(val pos: Int, val newNote: String): EventVm()
}