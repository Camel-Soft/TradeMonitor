package com.camelsoft.trademonitor._presentation.activity_main.fragment_checker

import com.camelsoft.trademonitor._presentation.models.MScan

sealed class EventsVmChecker {
    data class PerformRequest(val mScan: MScan): EventsVmChecker()
}
