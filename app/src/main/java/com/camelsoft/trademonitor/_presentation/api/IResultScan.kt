package com.camelsoft.trademonitor._presentation.api

import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor.common.events.EventsSync

interface IResultScan {
    fun actionScan(scan: EventsSync<MScan>)
}