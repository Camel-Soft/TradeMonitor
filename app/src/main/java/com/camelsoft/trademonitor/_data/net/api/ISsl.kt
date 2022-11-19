package com.camelsoft.trademonitor._data.net.api

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

interface ISsl {
    fun getSslContext(): SSLContext
    fun getTrustManagerFactory(): TrustManagerFactory
}
