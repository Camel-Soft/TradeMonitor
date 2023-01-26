package com.camelsoft.trademonitor._domain.use_cases.use_cases_repository

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.net.api.retro.NetApiInSouthUpload
import com.camelsoft.trademonitor._presentation.api.repo.IInSouthUpload
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.events.EventsOkInEr
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException

class UseCaseRepoInSouthUploadImpl(
    private val netApiInSouthUpload: NetApiInSouthUpload
    ): IInSouthUpload  {

    override suspend fun inSouthUpload(file: File, south: String): EventsOkInEr<String> {
        try {
            val response = netApiInSouthUpload.inSouthUpload(
                file = MultipartBody.Part.createFormData(
                    file.name,
                    file.name,
                    file.asRequestBody()
                ),
                south = south
            )
            if (response.code() != 200)
                return EventsOkInEr.Error("[UseCaseRepoInSouthUploadImpl.inSouthUpload] [server] - ${response.code()} ${response.message()}")
            else {
                response.body()?.let { mMessage ->
                    when (mMessage.status) {
                        "ok" -> return EventsOkInEr.Success(mMessage.message)
                        "info" -> return EventsOkInEr.Info(mMessage.message)
                        "error" -> return EventsOkInEr.Error("[UseCaseRepoInSouthUploadImpl.inSouthUpload] [server] ${mMessage.message}")
                        else -> return EventsOkInEr.Error("[UseCaseRepoInSouthUploadImpl.inSouthUpload] [server] ${getAppContext().resources.getString(R.string.error_response_status)} - ${mMessage.status}")
                    }
                }?: return EventsOkInEr.Error("[UseCaseRepoInSouthUploadImpl.inSouthUpload] ${getAppContext().resources.getString(R.string.error_response_body)}")
            }
        }
        catch (e: ConnectException) {
            e.printStackTrace()
            return EventsOkInEr.Error(getAppContext().resources.getString(R.string.error_сonnect))
        }
        catch (e: SSLPeerUnverifiedException) {
            e.printStackTrace()
            return EventsOkInEr.Error(getAppContext().resources.getString(R.string.error_ssl_unverified))
        }
        catch (e: SSLHandshakeException) {
            e.printStackTrace()
            return EventsOkInEr.Error(getAppContext().resources.getString(R.string.error_handshake))
        }
        catch (e: UnknownHostException) {
            e.printStackTrace()
            return EventsOkInEr.Error(getAppContext().resources.getString(R.string.error_unknown_host))
        }
        catch (e: InterruptedIOException) {
            e.printStackTrace()
            return EventsOkInEr.Error(getAppContext().resources.getString(R.string.error_interrupted_io))
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsOkInEr.Error("[UseCaseRepoInSouthUploadImpl.fileUpload] ${e.localizedMessage}")
        }
    }
}
