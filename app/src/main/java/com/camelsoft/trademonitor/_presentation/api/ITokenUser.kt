package com.camelsoft.trademonitor._presentation.api

import com.camelsoft.trademonitor._presentation.models.user.MUser

interface ITokenUser {
    suspend fun authUserBase(token: String?): Boolean
    suspend fun tokenToMUser(token: String?): MUser?
    suspend fun authUserDev(token: String?): Boolean
    suspend fun authUserSrv(token: String?): Boolean
}
