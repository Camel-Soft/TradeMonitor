package com.camelsoft.trademonitor._presentation.api

import com.camelsoft.trademonitor._presentation.models.MStringString

interface IChZnParam {
    suspend fun getInnList(): ArrayList<MStringString>
}