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
            if (response.code() != 200)
                return EventsNet.Error("[HelloImpl.hello] [server] - ${response.code()} ${response.message()}")
            else {
                response.body()?.let { mMessage ->
                    when (mMessage.status) {
                        "ok" -> return EventsNet.Success(mMessage.message)
                        "info" -> return EventsNet.Info(mMessage.message)
                        "error" -> return EventsNet.Error("[HelloImpl.hello] [server] ${mMessage.message}")
                        else -> return EventsNet.Error("[HelloImpl.hello] [server] ${getAppContext().resources.getString(R.string.error_response_status)} - ${mMessage.status}")
                    }
                }?: return EventsNet.Error("[HelloImpl.hello] ${getAppContext().resources.getString(R.string.error_response_body)}")
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
            if (response.code() != 200)
                return EventsNet.Error("[HelloImpl.helloAuth] [server] - ${response.code()} ${response.message()}")
            else {
                response.body()?.let { mMessage ->
                    when (mMessage.status) {
                        "ok" -> return EventsNet.Success(mMessage.message)
                        "info" -> return EventsNet.Info(mMessage.message)
                        "error" -> return EventsNet.Error("[HelloImpl.helloAuth] [server] ${mMessage.message}")
                        else -> return EventsNet.Error("[HelloImpl.helloAuth] [server] ${getAppContext().resources.getString(R.string.error_response_status)} - ${mMessage.status}")
                    }
                }?: return EventsNet.Error("[HelloImpl.helloAuth] ${getAppContext().resources.getString(R.string.error_response_body)}")
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
