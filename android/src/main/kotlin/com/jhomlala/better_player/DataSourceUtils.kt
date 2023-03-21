package com.jhomlala.better_player

import android.net.Uri
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import java.util.Locale

internal object DataSourceUtils {
    private const val USER_AGENT = "user-agent"
    private const val USER_AGENT_PROPERTY = "http.agent"

    @JvmStatic
    fun getUserAgent(headers: Map<String, String>?): String? {
        var userAgent = System.getProperty(USER_AGENT_PROPERTY)
        headers?.forEach {
            if (it.key.lowercase(Locale.getDefault()) == USER_AGENT) {
                userAgent = it.value
            }
        }
        return userAgent
    }

    @JvmStatic
    fun getDataSourceFactory(
        userAgent: String?,
        headers: Map<String, String>?
    ): DataSource.Factory {
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
            .setUserAgent(userAgent)
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS)
            .setReadTimeoutMs(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS)
        if (headers != null) {
            val notNullHeaders = mutableMapOf<String, String>()
            headers.forEach { entry ->
                notNullHeaders[entry.key] = entry.value
            }
            (dataSourceFactory as DefaultHttpDataSource.Factory).setDefaultRequestProperties(
                notNullHeaders
            )
        }
        return dataSourceFactory
    }

    @JvmStatic
    fun isHTTP(uri: Uri?): Boolean {
        if (uri == null || uri.scheme == null) {
            return false
        }
        val scheme = uri.scheme
        return scheme == "http" || scheme == "https"
    }
}