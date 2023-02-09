package com.camelsoft.trademonitor._domain.use_cases.use_cases_net

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.net.api.retro.NetApiInn
import com.camelsoft.trademonitor._presentation.api.repo.IInn
import com.camelsoft.trademonitor._presentation.models.MInn
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InterruptedIOException
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException

class InnImpl(private val netApiInn: NetApiInn): IInn {
    override suspend fun getInn(): EventsNet<List<MInn>> {
        try {
            val response = netApiInn.getConfInn()
            if (response.code() != 200)
                return EventsNet.Error("[InnImpl.getInn] [server] - ${response.code()} ${response.message()}")
            else {
                response.body()?.let { mMessage ->
                    when (mMessage.status) {
                        "ok" -> {
                            val listMInnType: Type = object : TypeToken<ArrayList<MInn>>() {}.type
                            return EventsNet.Success(Gson().fromJson(mMessage.message, listMInnType))
                        }
                        "info" -> return EventsNet.Info(mMessage.message)
                        "error" -> return EventsNet.Error("[InnImpl.getInn] [server] ${mMessage.message}")
                        else -> return EventsNet.Error("[InnImpl.getInn] [server] ${getAppContext().resources.getString(R.string.error_response_status)} - ${mMessage.status}")
                    }
                }?: return EventsNet.Error("[InnImpl.getInn] ${getAppContext().resources.getString(R.string.error_response_body)}")
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
            return EventsNet.Error("[InnImpl.getInn] ${e.localizedMessage}")
        }
    }
}
