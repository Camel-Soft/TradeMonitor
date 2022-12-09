package com.camelsoft.trademonitor._presentation.api.repo

import com.camelsoft.trademonitor._domain.use_cases.use_cases_net.EventsNet

interface IHello {
    suspend fun hello(): EventsNet<String>
    suspend fun helloAuth(): EventsNet<String>
}
