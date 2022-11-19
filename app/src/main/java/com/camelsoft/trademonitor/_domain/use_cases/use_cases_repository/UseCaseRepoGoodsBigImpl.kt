package com.camelsoft.trademonitor._domain.use_cases.use_cases_repository

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.net.api.retro.NetApiScan
import com.camelsoft.trademonitor._presentation.api.IGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.google.gson.Gson
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException

class UseCaseRepoGoodsBigImpl(private val netApiScan: NetApiScan): IGoods {

    override suspend fun getGoodsBig(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig> {
        try {
            val response = netApiScan.responseGoodsBig(body = mGoodsBig)
            if (response.code() != 200)
                return EventsGoods.Error("[UseCaseRepoGoodsBigImpl.getGoodsBig] [server] - ${response.code()} ${response.message()}")
            else {
                response.body()?.let { mMessage ->
                    when (mMessage.status) {
                        "ok" -> return EventsGoods.Success(Gson().fromJson(mMessage.message, MGoodsBig::class.java))
                        "update" -> return EventsGoods.Update(mMessage.message)
                        "notfound" -> return EventsGoods.UnSuccess(mMessage.message)
                        "info" -> return EventsGoods.Info(mMessage.message)
                        "error" -> return EventsGoods.Error("[UseCaseRepoGoodsBigImpl.getGoodsBig] [server] ${mMessage.message}")
                        else -> return EventsGoods.Error("[UseCaseRepoGoodsBigImpl.getGoodsBig] [server] ${getAppContext().resources.getString(R.string.error_response_status)} - ${mMessage.status}")
                    }
                }?: return EventsGoods.Error("[UseCaseRepoGoodsBigImpl.getGoodsBig] ${getAppContext().resources.getString(R.string.error_response_body)}")
            }
        }
        catch (e: ConnectException) {
            e.printStackTrace()
            return EventsGoods.Error(getAppContext().resources.getString(R.string.error_сonnect))
        }
        catch (e: SSLPeerUnverifiedException) {
            e.printStackTrace()
            return EventsGoods.Error(getAppContext().resources.getString(R.string.error_ssl_unverified))
        }
        catch (e: SSLHandshakeException) {
            e.printStackTrace()
            return EventsGoods.Error(getAppContext().resources.getString(R.string.error_handshake))
        }
        catch (e: UnknownHostException) {
            e.printStackTrace()
            return EventsGoods.Error(getAppContext().resources.getString(R.string.error_unknown_host))
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsGoods.Error("[UseCaseRepoGoodsBigImpl.getGoodsBig] ${e.localizedMessage}")
        }
    }
}
