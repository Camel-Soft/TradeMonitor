package com.camelsoft.trademonitor._domain.use_cases.use_cases_repository

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.net.api.retro.NetApiOfflBase
import com.camelsoft.trademonitor._presentation.api.repo.IOfflBase
import com.camelsoft.trademonitor._presentation.utils.isStatusExists
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.events.EventsSync
import okhttp3.ResponseBody
import java.io.File
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException

class UseCaseRepoOfflBaseImpl(private val netApiOfflBase: NetApiOfflBase): IOfflBase {

    override suspend fun getOfflBase(): EventsGoods<File> {
        try {
            val response = netApiOfflBase.responseOfflBase()

            if (response.code() != 200)
                return EventsGoods.Error("[UseCaseRepoOfflBaseImpl.getOfflBase] ${getAppContext().resources.getString(R.string.error_not_200)} - ${response.code()} ${response.message()}")

            if (!response.headers().isStatusExists())
                return EventsGoods.Error("[UseCaseRepoOfflBaseImpl.getOfflBase] ${getAppContext().resources.getString(R.string.error_header_status_not_found)}")

            when (response.headers()["status"]) {
                "unauthorized" -> {
                    val fff: ResponseBody? = response.body()

                }
                "update" -> {}
                "unsuccess" -> {}
                "error" -> {}
                "success" -> {}
                null -> {}
                else -> {}
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
        catch (e: InterruptedIOException) {
            e.printStackTrace()
            return EventsGoods.Error(getAppContext().resources.getString(R.string.error_interrupted_io))
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsGoods.Error("[UseCaseRepoOfflBaseImpl.getOfflBase] ${e.localizedMessage}")
        }
    }

    override suspend fun unzipOfflBase(zipFile: File): EventsSync<File> {

    }
}
