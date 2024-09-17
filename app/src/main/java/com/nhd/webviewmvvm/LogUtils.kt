package com.nhd.webviewmvvm

import android.util.Log

object LogUtils {
    fun d(tag: String?, message: String) {
        Log.d("LogUtils-APP-$tag", message)
    }

    fun e(tag: String?, message: String, exception: Exception?) {
        Log.e("LogUtils-APP-$tag", "$message: ${exception?.stackTraceToString()}")
    }
}

fun Exception.getErrorMessage(): String {
    return localizedMessage ?: "Unknown error"
}