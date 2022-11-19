package com.camelsoft.trademonitor._data.net.servers

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.net.api.ISsl
import com.camelsoft.trademonitor.common.App
import java.io.BufferedInputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

class SslImpl : ISsl {
    private val keyStore = prepKeyStore()
    private val trustManagerFactory = prepTrustManagerFactory()
    private val sslContext = prepSSLContext()

    override fun getSslContext(): SSLContext = sslContext
    override fun getTrustManagerFactory(): TrustManagerFactory = trustManagerFactory

    private fun prepKeyStore(): KeyStore {
        try {
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val cert = BufferedInputStream(App.getAppContext().resources.openRawResource(R.raw.tms))
            val ca = certificateFactory.generateCertificate(cert)
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)
            cert.close()
            return keyStore
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[SslImpl.prepKeyStore] ${e.message}")
        }
    }

    private fun prepTrustManagerFactory(): TrustManagerFactory {
        try {
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)
            return trustManagerFactory
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[SslImpl.prepTrustManagerFactory] ${e.message}")
        }
    }

    private fun prepSSLContext(): SSLContext {
        try {
            val sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, trustManagerFactory.trustManagers, null)
            return sslContext
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[SslImpl.prepSSLContext] ${e.message}")
        }
    }
}
