package com.camelsoft.trademonitor._domain.use_cases.use_cases_net

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.net.api.NetApiSign
import com.camelsoft.trademonitor._presentation.api.ISign
import com.camelsoft.trademonitor._presentation.models.user.MUserSign
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import java.net.ConnectException
import javax.net.ssl.SSLPeerUnverifiedException

class SignImpl(private val netApiSign: NetApiSign): ISign {
    override suspend fun signUp(mUserSign: MUserSign): EventsNet<String> {
        try {
            val response = netApiSign.signUp(body = mUserSign)
            when (response.code()) {
                200 -> return EventsNet.Success(getAppContext().resources.getString(R.string.ok))
                400 -> response.body()?.let {
                    return EventsNet.Error(it)
                }?: return EventsNet.Error("[SignImpl.signUp] ${getAppContext().resources.getString(R.string.from_server)} - ${response.code()} ${response.message()}")
                401 -> return EventsNet.Error(getAppContext().resources.getString(R.string.error_unauthorized))
                409 -> response.body()?.let {
                    return EventsNet.Error(it)
                }?: return EventsNet.Error("[SignImpl.signUp] ${getAppContext().resources.getString(R.string.from_server)} - ${response.code()} ${response.message()}")
                500 -> response.body()?.let {
                    return EventsNet.Error(it)
                }?: return EventsNet.Error("[SignImpl.signUp] ${getAppContext().resources.getString(R.string.from_server)} - ${response.code()} ${response.message()}")
                else -> return EventsNet.Error("[SignImpl.signUp] ${getAppContext().resources.getString(R.string.from_server)} - ${response.code()} ${response.message()}")
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
        catch (e: Exception) {
            e.printStackTrace()
            return EventsNet.Error("[SignImpl.signUp] ${e.localizedMessage}")
        }
    }

    override suspend fun signIn(mUserSign: MUserSign): EventsNet<String> {
        try {
            val response = netApiSign.signIn(body = mUserSign)
            when (response.code()) {
                200 -> {
                    if (response.isSuccessful && response.body() != null)
                        return EventsNet.Success(response.body()!!)
                    else
                        return EventsNet.Error("[SignImpl.signIn] 200 ${getAppContext().resources.getString(R.string.error_response_body)}")
                }
                400 -> response.body()?.let {
                    return EventsNet.Error(it)
                }?: return EventsNet.Error("[SignImpl.signIn] ${getAppContext().resources.getString(R.string.from_server)} - ${response.code()} ${response.message()}")
                401 -> return EventsNet.Error(getAppContext().resources.getString(R.string.error_unauthorized))
                409 -> response.body()?.let {
                    return EventsNet.Error(it)
                }?: return EventsNet.Error("[SignImpl.signIn] ${getAppContext().resources.getString(R.string.from_server)} - ${response.code()} ${response.message()}")
                500 -> response.body()?.let {
                    return EventsNet.Error(it)
                }?: return EventsNet.Error("[SignImpl.signIn] ${getAppContext().resources.getString(R.string.from_server)} - ${response.code()} ${response.message()}")
                else -> return EventsNet.Error("[SignImpl.signIn] ${getAppContext().resources.getString(R.string.from_server)} - ${response.code()} ${response.message()}")
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
        catch (e: Exception) {
            e.printStackTrace()
            return EventsNet.Error("[SignImpl.signIn] ${e.localizedMessage}")
        }
    }
}
