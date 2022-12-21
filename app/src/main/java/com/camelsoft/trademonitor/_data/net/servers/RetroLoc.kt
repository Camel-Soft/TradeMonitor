package com.camelsoft.trademonitor._data.net.servers

import com.camelsoft.trademonitor._data.net.api.ISsl
import com.camelsoft.trademonitor._data.net.interceptors.TokenInterceptor
import com.camelsoft.trademonitor._presentation.utils.prepareUrl
import com.camelsoft.trademonitor.common.settings.Settings
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.X509TrustManager

class RetroLoc @Inject constructor(
    private val iSsl: ISsl,
    private val tokenInterceptor: TokenInterceptor,
    private val settings: Settings
) {
    fun makeRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .sslSocketFactory(iSsl.getSslContext().socketFactory, iSsl.getTrustManagerFactory().trustManagers[0] as X509TrustManager)
            .hostnameVerifier { hostName, sslSession -> hostName.equals(sslSession.peerHost) }
            .addInterceptor(tokenInterceptor)
            .build()

        try {
            return Retrofit.Builder()
                .baseUrl(settings.getConnSrvLoc().prepareUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        catch (e: Exception) {
            e.printStackTrace()
            return Retrofit.Builder()
                .baseUrl("https://bad.address")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
    }
}
