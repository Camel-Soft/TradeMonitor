package com.camelsoft.trademonitor._presentation.api.scan

import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor.common.events.EventsSync

interface IResultScanList {
    fun actionScanList(scanList: EventsSync<ArrayList<MScan>>)
}