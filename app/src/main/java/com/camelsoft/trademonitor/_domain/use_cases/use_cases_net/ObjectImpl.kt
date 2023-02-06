package com.camelsoft.trademonitor._domain.use_cases.use_cases_net

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.net.api.retro.NetApiObject
import com.camelsoft.trademonitor._presentation.api.repo.IObject
import com.camelsoft.trademonitor._presentation.models.MAddress
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InterruptedIOException
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException

class ObjectImpl(private val netApiObject: NetApiObject): IObject {
    override suspend fun getObjects(): EventsNet<List<MAddress>> {
        try {
            val response = netApiObject.getConfObjects()
            if (response.code() != 200)
                return EventsNet.Error("[ObjectImpl.getObjects] [server] - ${response.code()} ${response.message()}")
            else {
                response.body()?.let { mMessage ->
                    when (mMessage.status) {
                        "ok" -> {
                            val listMAddressType: Type = object : TypeToken<ArrayList<MAddress>>() {}.type
                            return EventsNet.Success(Gson().fromJson(mMessage.message, listMAddressType))
                        }
                        "info" -> return EventsNet.Info(mMessage.message)
                        "error" -> return EventsNet.Error("[ObjectImpl.getObjects] [server] ${mMessage.message}")
                        else -> return EventsNet.Error("[ObjectImpl.getObjects] [server] ${getAppContext().resources.getString(R.string.error_response_status)} - ${mMessage.status}")
                    }
                }?: return EventsNet.Error("[ObjectImpl.getObjects] ${getAppContext().resources.getString(R.string.error_response_body)}")
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
        catch (e: InterruptedIOException) {
            e.printStackTrace()
            return EventsNet.Error(getAppContext().resources.getString(R.string.error_interrupted_io))
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsNet.Error("[ObjectImpl.getObjects] ${e.localizedMessage}")
        }
    }
}
