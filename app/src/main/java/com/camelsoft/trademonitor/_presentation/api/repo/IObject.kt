package com.camelsoft.trademonitor._presentation.api.repo

import com.camelsoft.trademonitor._domain.use_cases.use_cases_net.EventsNet
import com.camelsoft.trademonitor._presentation.models.MAddress

interface IObject {
    suspend fun getObjects(): EventsNet<List<MAddress>>
}
