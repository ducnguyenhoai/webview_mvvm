package com.nhd.webviewmvvm

import android.content.Context
import com.fiaivn.finsdk.util.StorageUtils

object LocalStorageUtils {



    fun saveAppVersion(context: Context, name: String, version: Int) {
        StorageUtils.putLocalStorage(
            context,
            "version$name",
            version,
            -1,
        )
    }

    fun loadAppVersion(context: Context, name: String): Int {
        return StorageUtils.getLocalStorage(
            context,
            "version$name",
            -1,
            -1,
        ) as Int
    }

}