package com.camelsoft.trademonitor._presentation.api.repo

import com.camelsoft.trademonitor.common.events.EventsProgress
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IOfflBase {
    suspend fun getOfflBase(): Flow<EventsProgress<File>>
    suspend fun unzipOfflBase(zipFile: File): Flow<EventsProgress<File>>
    suspend fun publishOfflBase(sourceFolder: File): Flow<EventsProgress<File>>
}
