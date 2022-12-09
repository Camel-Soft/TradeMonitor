package com.camelsoft.trademonitor._presentation.api.repo

import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor.common.events.EventsSync
import java.io.File

interface IOfflBase {
    suspend fun getOfflBase(): EventsGoods<File>
    suspend fun unzipOfflBase(zipFile: File): EventsSync<File>
}
