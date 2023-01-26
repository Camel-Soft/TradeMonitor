package com.camelsoft.trademonitor._presentation.api.repo

import com.camelsoft.trademonitor.common.events.EventsOkInEr
import java.io.File

interface IInSouthUpload {
    suspend fun inSouthUpload(file: File, south: String): EventsOkInEr<String>
}
