package com.camelsoft.trademonitor._domain.use_cases.use_cases_net

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.net.api.retro.NetApiSign
import com.camelsoft.trademonitor._presentation.api.ISign
import com.camelsoft.trademonitor._presentation.models.user.MUserSign
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException

class SignImpl(private val netApiSign: NetApiSign): ISign {
    override suspend fun signUp(mUserSign: MUserSign): EventsNet<String> {
        try {
            val response = netApiSign.signUp(body = mUserSign)
            if (response.code() != 200)
                return EventsNet.Error("[SignImpl.signUp] [server] - ${response.code()} ${response.message()}")
            else {
                response.body()?.let { mMessage ->
                    when (mMessage.status) {
                        "ok" -> return EventsNet.Success(mMessage.message)
                        "info" -> return EventsNet.Info(mMessage.message)
                        "error" -> return EventsNet.Error("[SignImpl.signUp] [server] ${mMessage.message}")
                        else -> return EventsNet.Error("[SignImpl.signUp] [server] ${getAppContext().resources.getString(R.string.error_response_status)} - ${mMessage.status}")
                    }
                }?: return EventsNet.Error("[SignImpl.signUp] ${getAppContext().resources.getString(R.string.error_response_body)}")
            }
        }
        catch (e: ConnectException) {
            e.printStackTrace()
            return EventsNet.Error(getAppContext().resources.getString(R.string.error_сonnect))
        }
        catch (e: SSLPeerUnverifiedException) {
            e.printStackTrace()
            return EventsNet.Error(getAppContext().resources.getString(R.string.error_ssl_unverified))
        }
        catch (e: SSLHandshakeException) {
            e.printStackTrace()
            return EventsNet.Error(getAppContext().resources.getString(R.string.error_handshake))
        }
        catch (e: UnknownHostException) {
            e.printStackTrace()
            return EventsNet.Error(getAppContext().resources.getString(R.string.error_unknown_host))
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsNet.Error("[SignImpl.signUp] ${e.localizedMessage}")
        }
    }

    override suspend fun signIn(mUserSign: MUserSign): EventsNet<String> {
        try {
            val response = netApiSign.signIn(body = mUserSign)
            if (response.code() != 200)
                return EventsNet.Error("[SignImpl.signIn] [server] - ${response.code()} ${response.message()}")
            else {
                response.body()?.let { mMessage ->
                    when (mMessage.status) {
                        "ok" -> return EventsNet.Success(mMessage.message)
                        "info" -> return EventsNet.Info(mMessage.message)
                        "error" -> return EventsNet.Error("[SignImpl.signIn] [server] ${mMessage.message}")
                        else -> return EventsNet.Error("[SignImpl.signIn] [server] ${getAppContext().resources.getString(R.string.error_response_status)} - ${mMessage.status}")
                    }
                }?: return EventsNet.Error("[SignImpl.signIn] ${getAppContext().resources.getString(R.string.error_response_body)}")
            }
        }
        catch (e: ConnectException) {
            e.printStackTrace()
            return EventsNet.Error(getAppContext().resources.getString(R.string.error_сonnect))
        }
        catch (e: SSLPeerUnverifiedException) {
            e.printStackTrace()
            return EventsNet.Error(getAppContext().resources.getString(R.string.error_ssl_unverified))
        }
        catch (e: SSLHandshakeException) {
            e.printStackTrace()
            return EventsNet.Error(getAppContext().resources.getString(R.string.error_handshake))
        }
        catch (e: UnknownHostException) {
            e.printStackTrace()
            return EventsNet.Error(getAppContext().resources.getString(R.string.error_unknown_host))
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsNet.Error("[SignImpl.signIn] ${e.localizedMessage}")
        }
    }
}
