package com.camelsoft.trademonitor._presentation.api

import com.camelsoft.trademonitor._presentation.models.MStringString

interface IRemoteConfigFirebase {
    suspend fun getInnList(): ArrayList<MStringString>
    suspend fun getEnabled(): Boolean
}
