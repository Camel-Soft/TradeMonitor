package com.camelsoft.trademonitor._presentation.api.repo

import com.camelsoft.trademonitor._domain.use_cases.use_cases_net.EventsNet
import com.camelsoft.trademonitor._presentation.models.MInn

interface IInn {
    suspend fun getInn(): EventsNet<List<MInn>>
}
