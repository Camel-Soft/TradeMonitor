package com.camelsoft.trademonitor._data.net.interceptors

import com.camelsoft.trademonitor._data.net.managers.TokenManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(private val tokenManager: TokenManager): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val rowToken: String? = tokenManager.getToken()
        rowToken?.let { token ->
            return chain.proceed(addToken(token = token, request = request))
        }?: return chain.proceed(request = request)
    }

    private fun addToken(token: String, request: Request): Request {
        return request.newBuilder()
            .header("Bearer", token)
            .build()
    }
}
