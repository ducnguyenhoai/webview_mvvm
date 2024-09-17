package com.nhd.webviewmvvm

import android.webkit.JavascriptInterface
import androidx.lifecycle.lifecycleScope
import com.nhd.webviewmvvm.models.RequestMessage
import com.nhd.webviewmvvm.models.ResponseMessage
import com.nhd.webviewmvvm.models.ResponseMessageData
import com.nhd.webviewmvvm.models.WebCommand
import com.nhd.webviewmvvm.viewmodels.DataViewModel
import com.nhd.webviewmvvm.viewmodels.DeviceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class MainJavascriptInterface(private val webViewInterface: WebViewInterface) {

    @Inject
    lateinit var dataViewModel: DataViewModel

    @Inject
    lateinit var deviceViewModel: DeviceViewModel

    @JavascriptInterface
    fun postMessage(requestMessageStr: String?): Boolean {
        if (requestMessageStr == null) {
            return false
        }

        LogUtils.d("MYLOG", "messageStr: $requestMessageStr")

        val requestMessage: RequestMessage = try {
            RequestMessage.fromJSON(JSONObject(requestMessageStr))
        } catch (e: JSONException) {
            return false
        }
        LogUtils.d("MYLOG", "webMessage: $requestMessage")

        val webCommand: WebCommand? =
            WebCommand.entries.find { it.command == requestMessage.command || it.newCommand == requestMessage.command }
        LogUtils.d("MYLOG", "webCommand: $webCommand")

        GlobalScope.launch(Dispatchers.IO) {
            val responseMessageData: ResponseMessageData? = when (webCommand) {
                WebCommand.SCAN_QR -> null
                WebCommand.COPY_TO_CLIPBOARD -> dataViewModel.copyToClipboard(requestMessage)
                WebCommand.GET_FROM_CLIPBOARD -> dataViewModel.getFromClipboard(requestMessage)
                WebCommand.CAPTURE_SCREEN -> null
                WebCommand.GET_QR_FROM_FILE -> null
                WebCommand.GET_QR_FROM_IMAGE -> null
                WebCommand.GET_QR_FROM_CAMERA -> null
                WebCommand.GET_QR_FROM_SCREEN -> null
                WebCommand.BACKUP_DATA -> null
                WebCommand.TAKE_PICTURE -> deviceViewModel.takePicture(requestMessage)
                WebCommand.SELECT_IMAGE -> null
                WebCommand.SET_STATUS_BIOMETRIC -> null
                WebCommand.SET_STATUS_WATCH_CONFIRM -> null
                WebCommand.GET_BACKUP_FILES -> null
                WebCommand.SHARE -> null
                WebCommand.SHARE_ITEM -> null
                WebCommand.UNZIP_FILE_RESTORE -> null
                WebCommand.RESTORE_DATA -> null
                WebCommand.OPEN_SERVER_SOCKET -> null
                WebCommand.CLOSE_SERVER_SOCKET -> null
                WebCommand.SEND_FILE -> null
                WebCommand.CONNECT_TO_SERVER_SOCKET -> null
                WebCommand.SEND_MESSAGE_TO_SERVER -> null
                WebCommand.CLOSE_CLIENT_SOCKET -> null
                WebCommand.IMPORT_BY_FILE -> null
                WebCommand.GET_FILE_ZIP -> null
                WebCommand.GET_FILE -> null
                WebCommand.UNZIP_FILE -> null
                WebCommand.READ_ABI_STRING -> null
                WebCommand.OPEN_D_APP -> null
                WebCommand.CHECK_IS_ONLINE -> null
                WebCommand.GET_STATUS_CONNECTED -> null
                WebCommand.SEND_SMS_BY_DEFAULT_APP -> null
                WebCommand.SEND_MAIL_BY_SELECTABLE_APP -> null
                WebCommand.SEND_TEXT_TO_TELEGRAM -> null
                WebCommand.WATCH_APPROVE -> null
                WebCommand.SYNC_DATA_TO_WATCH -> null
                WebCommand.SHARE_SECRET_TO_WATCH -> null
                WebCommand.GET_APP_INFO_FROM_URL -> null
                WebCommand.CHECKING_SIGN_APP -> null
                WebCommand.SEND_TRANSACTION -> null
                WebCommand.EXECUTE_SMART_CONTRACT -> null
                WebCommand.CHECK_D_APP_EXIST -> null
                WebCommand.WRITE_TO_LOCAL_STORAGE -> null
                WebCommand.SHARE_D_APP_TO_PUBLIC -> null
                WebCommand.CHECK_DEVICE_HAS_NOTCH -> null
                WebCommand.VIBRATE -> null
                WebCommand.GET_BIOMETRIC_TYPE -> null
                WebCommand.RELOAD_ALL -> null
                WebCommand.GET_ALL_D_APP_NO_GROUP -> null
                WebCommand.SHOW_BOTTOM -> null
                WebCommand.HIDE_BOTTOM -> null
                WebCommand.SET_BOTTOM_CONTENT -> null
                WebCommand.HANDLE_EVENT -> null
                WebCommand.TEL -> null
                WebCommand.SELECT_DATE -> null
                WebCommand.DATE_SELECTED -> null
                WebCommand.CLOSE_CALENDAR -> null
                WebCommand.ON_CLICK -> null
                WebCommand.OPEN_URL -> null
                null -> null
            }

            responseMessageData?.let {
                webViewInterface.postMessageToWeb(
                    ResponseMessage.fromRequest(
                        request = requestMessage,
                        data = it,
                    )
                )
            }
        }

        return true
    }
}