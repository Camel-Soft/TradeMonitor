package com.camelsoft.trademonitor._domain.use_cases.use_cases_security

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.camelsoft.trademonitor._data.net.managers.TokenManager
import com.camelsoft.trademonitor._domain.api.ITokenUser
import com.camelsoft.trademonitor._domain.api.ITokenVerifier
import com.camelsoft.trademonitor._presentation.models.user.MUser

class TokenVerifierImpl(
    private val tokenUser: ITokenUser,
    private val tokenManager: TokenManager
    ) : ITokenVerifier {

    private val _mUser = MutableLiveData<MUser?>()
    val mUser: LiveData<MUser?> = _mUser

    private val observerToToken = Observer<String?> { token->
        try {
            if (token == null) _mUser.value = null
            else
                if (!tokenUser.authUserDev(token)) _mUser.value = null
                else _mUser.value = tokenUser.tokenToMUser(token)
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[TokenVerifierImpl.observerToToken] ${e.localizedMessage}")
        }
    }

    init {
        tokenManager.token.observeForever(observerToToken)
    }

    override suspend fun setNewToken(token: String?): Boolean {
        try {
            if (token == null) {
                tokenManager.putToken(null)
                return false
            }

            if (!tokenUser.authUserDev(token)) {
                tokenManager.putToken(null)
                return false
            }
            else {
                tokenManager.putToken(token)
                return true
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[TokenVerifierImpl.setNewToken] ${e.localizedMessage}")
        }
    }

    override suspend fun verifyExistToken(): Boolean {
        try {
            val token = tokenManager.getToken() ?: return false
            if (!tokenUser.authUserDev(token)) {
                tokenManager.putToken(null)
                return false
            }
            else return true
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[TokenVerifierImpl.verifyExistToken] ${e.localizedMessage}")
        }
    }
}
