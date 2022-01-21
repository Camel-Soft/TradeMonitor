package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

import com.camelsoft.trademonitor._domain.models.MPriceColl

sealed class EventVm {
    object OnAddCollClick: EventVm()
    data class OnDeleteCollClick(val priceColl: MPriceColl): EventVm()
    data class OnUpdateCollClick(val priceColl: MPriceColl, val newNote: String): EventVm()
}