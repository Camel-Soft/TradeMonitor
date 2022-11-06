package com.camelsoft.trademonitor._presentation.api

import com.camelsoft.trademonitor._presentation.models.secondary.MStringString

interface IChZnParam {
    suspend fun getInnList(): ArrayList<MStringString>
}