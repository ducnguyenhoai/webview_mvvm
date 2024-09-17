package com.nhd.webviewmvvm.models

import org.json.JSONObject

data class ResponseMessage(
    val messageId: String,
    val command: String,
    val appId: String,
    val data: ResponseMessageData?,
    val isSocket: Boolean,
) {

    fun toJSON(): JSONObject {
        return JSONObject(
            mapOf(
                "messageId" to messageId,
                "command" to command,
                "appId" to appId,
                "data" to data?.toJSON(),
                "isSocket" to isSocket,
            )
        )
    }

    companion object {
        fun fromRequest(
            request: RequestMessage,
            data: ResponseMessageData,
            isSocket: Boolean = false,
        ): ResponseMessage {
            return ResponseMessage(
                messageId = request.messageId,
                command = request.command,
                appId = request.appId,
                data = data,
                isSocket = isSocket,
            )
        }
    }

}