package com.camelsoft.trademonitor._domain.api

import com.camelsoft.trademonitor._presentation.models.user.MUser

interface ITokenUser {
    fun authUserBase(token: String?): Boolean
    fun tokenToMUser(token: String?): MUser?
    fun authUserDev(token: String?): Boolean
    fun authUserSrv(token: String?): Boolean
}
