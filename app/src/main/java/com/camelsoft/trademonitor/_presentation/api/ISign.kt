package com.camelsoft.trademonitor._presentation.api

import com.camelsoft.trademonitor._domain.use_cases.use_cases_net.EventsNet
import com.camelsoft.trademonitor._presentation.models.user.MUserSign

interface ISign {
    suspend fun signUp(mUserSign: MUserSign): EventsNet<String>
    suspend fun signIn(mUserSign: MUserSign): EventsNet<String>
}
