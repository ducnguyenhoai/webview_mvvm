package com.nhd.webviewmvvm.models

import org.json.JSONObject

data class RequestMessage(
    val messageId: String,
    val command: String,
    var appId: String,
    var value: JSONObject,
) {
    companion object {
        fun fromJSON(json: JSONObject): RequestMessage {
            return RequestMessage(
                messageId = json.optString("messageId", ""),
                command = json.optString("command", ""),
                appId = json.optString("appId", ""),
                value = json.optJSONObject("value") ?: JSONObject(),
            )
        }
    }
}