package com.nhd.webviewmvvm.extensions

import android.os.Build
import com.nhd.webviewmvvm.CustomWebView

fun CustomWebView.userAgent(): String {
    val sdkInt = Build.VERSION.SDK_INT
    return when {
        sdkInt < Build.VERSION_CODES.KITKAT -> {
            "Mozilla/5.0 (Linux; Android 4.4.4; Nexus 5 Build/KRT16L) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.91 Mobile Safari/537.36"
        }

        sdkInt < Build.VERSION_CODES.LOLLIPOP -> {
            "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36"
        }

        sdkInt < Build.VERSION_CODES.Q -> {
            "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36"
        }

        else -> {
            "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.6367.82 Mobile Safari/537.36"
        }
    }
}