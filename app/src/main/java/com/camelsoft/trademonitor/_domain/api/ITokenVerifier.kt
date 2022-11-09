package com.camelsoft.trademonitor._domain.api

interface ITokenVerifier {
    suspend fun setNewToken(token: String?): Boolean  // Возвращает - верифицирован или нет
    suspend fun verifyExistToken(): Boolean
}
