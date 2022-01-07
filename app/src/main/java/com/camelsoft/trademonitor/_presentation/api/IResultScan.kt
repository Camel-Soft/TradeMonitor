package com.camelsoft.trademonitor._presentation.api

import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor.common.resource.ResSync

interface IResultScan {
    fun actionScan(scan: ResSync<MScan>)
}