package com.camelsoft.trademonitor._domain.use_cases.use_cases_security

import com.camelsoft.trademonitor._domain.api.ITelephony
import com.camelsoft.trademonitor._presentation.api.ITokenUser
import com.camelsoft.trademonitor._presentation.models.user.MDev
import com.camelsoft.trademonitor._presentation.models.user.MUser
import com.camelsoft.trademonitor.common.Constants.Companion.JWT_ISSUER
import com.camelsoft.trademonitor.common.Constants.Companion.JWT_JWK_USERS_PUBLIC
import com.google.gson.Gson
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.SignedJWT

class TokenUserImpl(private val telephony: ITelephony): ITokenUser {

    private val jwkPublic: RSAKey = RSAKey.parse(JWT_JWK_USERS_PUBLIC)
    private val verifier: JWSVerifier = RSASSAVerifier(jwkPublic)
    private lateinit var signedJWT: SignedJWT

    override suspend fun authUserBase(token: String?): Boolean {
        try {
            if (token == null) return false
            signedJWT = SignedJWT.parse(token)
            if (!signedJWT.verify(verifier)) return false
            if (System.currentTimeMillis() > signedJWT.jwtClaimsSet.getLongClaim("expTime")) return false
            if (signedJWT.jwtClaimsSet.issuer != JWT_ISSUER || signedJWT.jwtClaimsSet.subject != "user") return false
            return true
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override suspend fun tokenToMUser(token: String?): MUser? {
        try {
            if (!authUserBase(token)) return null
            val devs = Gson().fromJson<List<MDev>>(signedJWT.jwtClaimsSet.getStringClaim("devs"), MDev::class.java)
            return MUser(
                issuer = signedJWT.jwtClaimsSet.issuer,
                subject = signedJWT.jwtClaimsSet.subject,
                userId = signedJWT.jwtClaimsSet.getIntegerClaim("userId"),
                email = signedJWT.jwtClaimsSet.getStringClaim("email"),
                isCorp = signedJWT.jwtClaimsSet.getBooleanClaim("isCorp"),
                notes = "",
                isActiveDev = signedJWT.jwtClaimsSet.getBooleanClaim("isActiveDev"),
                isActiveSrv = signedJWT.jwtClaimsSet.getBooleanClaim("isActiveSrv"),
                licLevelDev = signedJWT.jwtClaimsSet.getIntegerClaim("licLevelDev"),
                licLevelSrv = signedJWT.jwtClaimsSet.getIntegerClaim("licLevelSrv"),
                expTime = signedJWT.jwtClaimsSet.getLongClaim("expTime"),
                devs = devs,
                srvs = emptyList()
            )
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[TokenUserImpl.tokenToMUser] ${e.localizedMessage}")
        }
    }

    override suspend fun authUserDev(token: String?): Boolean {
        try {
            val mUser = tokenToMUser(token) ?: return false
            if (!mUser.isActiveDev) return false
            val mId = telephony.getTelephonyItems()
            mUser.devs.forEach { mDev->
                if (mDev.devSdk == mId.sdk && mDev.devId == mId.id && mDev.devAid == mId.aid &&
                    mDev.isActiveDev && System.currentTimeMillis() < mDev.expTimeDev && mDev.devDateUnreg == 0L)
                    return true
            }
            return false
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[TokenUserImpl.authUserDev] ${e.localizedMessage}")
        }
    }

    override suspend fun authUserSrv(token: String?): Boolean {
        try {
            val mUser = tokenToMUser(token) ?: return false
            return mUser.isActiveSrv
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[TokenUserImpl.authUserSrv] ${e.localizedMessage}")
        }
    }
}
