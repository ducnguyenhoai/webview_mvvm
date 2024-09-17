package com.nhd.webviewmvvm.models

import org.json.JSONObject

data class ResponseMessageData(
    val success: Boolean,
    val message: String,
    val code: String,
    val data: Any? = null
) {
    fun toJSON(): JSONObject {
        return JSONObject(
            mapOf(
                "success" to success,
                "message" to message,
                "code" to code,
                "data" to data,
            )
        )
    }
}