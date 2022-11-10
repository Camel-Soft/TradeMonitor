package com.camelsoft.trademonitor._domain.use_cases.use_cases_repository

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.net.api.NetApiScan
import com.camelsoft.trademonitor._presentation.api.IGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import java.net.ConnectException
import javax.net.ssl.SSLPeerUnverifiedException

class UseCaseRepoGoodsBigImpl(private val netApiScan: NetApiScan): IGoods {

    override suspend fun getGoodsBig(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig> {
        try {
            val response = netApiScan.responseGoodsBig(body = mGoodsBig)
            when (response.code()) {
                // OK - Товар найден
                200 -> {
                    if (response.isSuccessful && response.body() != null)
                        return EventsGoods.Success(response.body()!!)
                    else
                        return EventsGoods.Error("[UseCaseRepoGoodsBigImpl.getGoodsBig] 200 ${getAppContext().resources.getString(R.string.error_response_body)}")
                }
                // No Content - Товар НЕ найден
                204 -> {
                    return EventsGoods.UnSuccess(getAppContext().resources.getString(R.string.goods_not_found))
                }
                // Reset Content - Идет обновление баз
                205 -> {
                    return EventsGoods.Update(getAppContext().resources.getString(R.string.database_update))
                }
                // Unauthorized - Запрос не авторизован сервером
                401 -> {
                    return EventsGoods.Error(getAppContext().resources.getString(R.string.error_unauthorized))
                }
                else -> {
                    return EventsGoods.Error("[UseCaseRepoGoodsBigImpl.getGoodsBig] ${getAppContext().resources.getString(R.string.from_server)} - ${response.code()} ${response.message()}")
                }
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
        catch (e: Exception) {
            e.printStackTrace()
            return EventsGoods.Error("[UseCaseRepoGoodsBigImpl.getGoodsBig] ${e.localizedMessage}")
        }
    }
}
