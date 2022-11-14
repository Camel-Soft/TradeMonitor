package com.camelsoft.trademonitor._data.net.servers

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.net.interceptors.TokenInterceptor
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedInputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class RetroMy {
    private val keyStore = getKeyStore()
    private val trustManagerFactory = getTrustManagerFactory()
    private val sslContext = getSSLContext()
    private val tokenInterceptor = TokenInterceptor()
    val retrofit = makeRetroMy()

    private fun makeRetroMy(): Retrofit {
        try {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .callTimeout(2, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .sslSocketFactory(sslContext.socketFactory, trustManagerFactory.trustManagers[0] as X509TrustManager)
                .hostnameVerifier { hostName, sslSession -> hostName.equals(sslSession.peerHost) }
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(tokenInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://81.28.174.175:7000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[RetroMy.makeRetroMy] ${e.message}")
        }
    }

    private fun getKeyStore(): KeyStore {
        try {
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val cert = BufferedInputStream(getAppContext().resources.openRawResource(R.raw.tms))
            val ca = certificateFactory.generateCertificate(cert)
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)
            cert.close()
            return keyStore
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[RetroMy.getKeyStore] ${e.message}")
        }
    }

    private fun getTrustManagerFactory(): TrustManagerFactory {
        try {
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)
            return trustManagerFactory
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[RetroMy.getTrustManagerFactory] ${e.message}")
        }
    }

    private fun getSSLContext(): SSLContext {
        try {
            val sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, trustManagerFactory.trustManagers, null)
            return sslContext
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[RetroMy.getSSLContext] ${e.message}")
        }
    }
}
