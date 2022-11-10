package com.camelsoft.trademonitor._domain.use_cases.use_cases_net

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.net.api.NetApiHello
import com.camelsoft.trademonitor._presentation.api.IHello
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import java.net.ConnectException
import javax.net.ssl.SSLPeerUnverifiedException

class HelloImpl(private val netApiHello: NetApiHello): IHello {
    override suspend fun hello(): EventsNet<String> {
        try {
            val response = netApiHello.hello()
            when (response.code()) {
                200 -> response.body()?.let {
                    return EventsNet.Success(it)
                }?: return EventsNet.Success("code is 200, but message is null")
                401 -> return EventsNet.Error(getAppContext().resources.getString(R.string.error_unauthorized))
                else -> return EventsNet.Error("[HelloImpl.hello] ${getAppContext().resources.getString(R.string.from_server)} - ${response.code()} ${response.message()}")
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
            return EventsNet.Error("[HelloImpl.hello] ${e.localizedMessage}")
        }
    }

    override suspend fun helloAuth(): EventsNet<String> {
        try {
            val response = netApiHello.helloAuthBoth()
            when (response.code()) {
                200 -> response.body()?.let {
                    return EventsNet.Success(it)
                }?: return EventsNet.Success("code is 200, but message is null")
                401 -> return EventsNet.Error(getAppContext().resources.getString(R.string.error_unauthorized))
                else -> return EventsNet.Error("[HelloImpl.helloAuth] ${getAppContext().resources.getString(R.string.from_server)} - ${response.code()} ${response.message()}")
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
            return EventsNet.Error("[HelloImpl.helloAuth] ${e.localizedMessage}")
        }
    }
}
