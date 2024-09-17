package com.nhd.webviewmvvm.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nhd.webviewmvvm.LogUtils
import com.nhd.webviewmvvm.getErrorMessage
import com.nhd.webviewmvvm.models.RequestMessage
import com.nhd.webviewmvvm.models.ResponseMessageData
import com.nhd.webviewmvvm.usecase.ClipboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

//Use for handle methods that interact with data (read/write text, read/write file, zip/unzip...)
@HiltViewModel
class DataViewModel @Inject constructor(
    private val clipboardUseCase: ClipboardUseCase,
    application: Application,
) : AndroidViewModel(application) {

    private val tag = this::class.java.simpleName

    suspend fun copyToClipboard(requestMessage: RequestMessage): ResponseMessageData =
        withContext(Dispatchers.IO) {
            try {
                val valueJSON: JSONObject = requestMessage.value
                val value: String = valueJSON.optString("value", "")
                if (value.isEmpty()) {
                    return@withContext ResponseMessageData(
                        success = false,
                        message = "Missing value",
                        code = "10",
                    )
                }

                clipboardUseCase.save(value)

                ResponseMessageData(
                    success = true,
                    message = "Copy clipboard success",
                    code = "20",
                )
            } catch (e: Exception) {
                LogUtils.e(tag, "${requestMessage.command} exception", e)
                e.printStackTrace()
                ResponseMessageData(
                    success = false,
                    message = e.getErrorMessage(),
                    code = "30",
                )
            }
        }

    suspend fun getFromClipboard(requestMessage: RequestMessage): ResponseMessageData =
        withContext(Dispatchers.IO) {
            try {
                val value: String = clipboardUseCase.load()
                ResponseMessageData(
                    success = true,
                    message = "Get from clipboard success",
                    code = "10",
                    data = mapOf(
                        "value" to value,
                    )
                )
            } catch (e: Exception) {
                LogUtils.e(tag, "${requestMessage.command} exception", e)
                ResponseMessageData(
                    success = false,
                    message = e.getErrorMessage(),
                    code = "20",
                )
            }
        }

}