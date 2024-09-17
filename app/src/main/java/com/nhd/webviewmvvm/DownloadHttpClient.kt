package com.nhd.webviewmvvm

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object DownloadHttpClient {
    val okHttp by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BASIC)
                    }
                }
            )
            .build()
    }
}