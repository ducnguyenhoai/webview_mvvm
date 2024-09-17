package com.nhd.webviewmvvm.usecase

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import javax.inject.Inject

class ClipboardUseCase @Inject constructor(private val application: Application) {

    fun save(value: String) {
        val clipboardManager: ClipboardManager =
            application.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("MetaNode", value)
        clipboardManager.setPrimaryClip(clip)
    }

    fun load(): String {
        val clipboardManager: ClipboardManager =
            application.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
    }

}