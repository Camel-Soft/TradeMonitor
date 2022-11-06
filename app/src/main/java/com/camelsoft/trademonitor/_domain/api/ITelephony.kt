package com.camelsoft.trademonitor._domain.api

import com.camelsoft.trademonitor._presentation.models.MId

interface ITelephony {
    suspend fun getTelephonyItems(): MId
}
