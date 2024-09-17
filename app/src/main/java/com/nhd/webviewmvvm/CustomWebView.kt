package com.nhd.webviewmvvm

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.nhd.webviewmvvm.extensions.userAgent
import com.nhd.webviewmvvm.models.ResponseMessage
import com.nhd.webviewmvvm.models.ResponseMessageData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONObject

@Suppress("DEPRECATION")
@SuppressLint("SetJavaScriptEnabled")
class CustomWebView : WebView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val job: Job = Job()
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancel()
    }

    init {
        clearCache(true)
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.javaScriptEnabled = true
        settings.allowFileAccess = true
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowContentAccess = true
        settings.domStorageEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true

        settings.userAgentString = userAgent()
        settings.setNeedInitialFocus(true)
        settings.mediaPlaybackRequiresUserGesture = false
        settings.setSupportMultipleWindows(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusable = FOCUSABLE_AUTO
        }
        isFocusable = true
        isFocusableInTouchMode = true
        setBackgroundColor(Color.TRANSPARENT)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)

        evaluateJavascript("window.webkit = { messageHandlers: { callbackHandler: window.androidHandler} }") {
            postMessageToWeb(
                ResponseMessage(
                    messageId = "",
                    command = "ready",
                    appId = "",
                    data = ResponseMessageData(
                        success = true,
                        message = "WebView is ready",
                        code = "",
                    ),
                    isSocket = true,
                )
            )
        }

        initWebViewClient()
    }

    private fun initWebViewClient() {
        webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                evaluateJavascript("window.webkit = { messageHandlers: { callbackHandler: window.androidHandler} }") {
                    postMessageToWeb(
                        ResponseMessage(
                            messageId = "",
                            command = "ready",
                            appId = "",
                            data = ResponseMessageData(
                                success = true,
                                message = "WebView is ready",
                                code = "",
                            ),
                            isSocket = true,
                        )
                    )
                }
            }
        }
    }

    fun postMessageToWeb(responseMessage: ResponseMessage) {
        post {
            val responseMessageJSON: JSONObject = responseMessage.toJSON()

            LogUtils.d("MYLOG", "postMessageToWeb: $responseMessageJSON")

            val script = "(function() {\n" +
                    "    var message = $responseMessageJSON;\n" +
                    "    window.postMessage(message)\n" +
                    "})()"

            evaluateJavascript(
                script
            ) {}
        }
    }
}