package com.nhd.webviewmvvm

import com.nhd.webviewmvvm.models.ResponseMessage

interface WebViewInterface {
    fun postMessageToWeb(responseMessage: ResponseMessage)
}