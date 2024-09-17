package com.nhd.webviewmvvm.viewmodels

import android.Manifest
import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nhd.webviewmvvm.BuildConfig
import com.nhd.webviewmvvm.models.RequestMessage
import com.nhd.webviewmvvm.models.ResponseMessage
import com.nhd.webviewmvvm.models.ResponseMessageData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(private val application: Application) :
    AndroidViewModel(application) {

    private val _takePictureIntent = MutableSharedFlow<Intent?>()
    val takePictureIntent: SharedFlow<Intent?> = _takePictureIntent

    private val _takePictureResponse = MutableSharedFlow<ResponseMessage?>()
    val takePictureResponse: SharedFlow<ResponseMessage?> = _takePictureResponse

    private fun checkCameraSupport(): Boolean {
        return application.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
                && application.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)
    }

    private var takePictureRequest: RequestMessage? = null
    private var takePictureFile: File? = null
    fun takePicture(requestMessage: RequestMessage): ResponseMessageData? {
        viewModelScope.launch(Dispatchers.IO) {
            var responseMessageData: ResponseMessageData? = null
            try {
                takePictureRequest = requestMessage
                val appId: String = requestMessage.appId
                val value: JSONObject = requestMessage.value
                val name: String =
                    value.optString("name", "").plus((System.currentTimeMillis() / 1000).toString())

                if (!checkCameraSupport()) {
                    responseMessageData = ResponseMessageData(
                        success = false,
                        message = "Camera is not support",
                        code = "10",
                    )
                    return@launch
                }
                if (ContextCompat.checkSelfPermission(
                        application.applicationContext,
                        Manifest.permission.CAMERA,
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    responseMessageData = ResponseMessageData(
                        success = false,
                        message = "Permission denied",
                        code = "20",
                    )
                    return@launch
                }

                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val componentName: ComponentName? =
                    takePictureIntent.resolveActivity(application.packageManager)

                if (componentName == null) {
                    responseMessageData = ResponseMessageData(
                        success = false,
                        message = "Can not take picture",
                        code = "30",
                    )
                    return@launch
                }

                val profileId = 1
                val path =
                    "${application.filesDir}/Profile-$profileId/$appId/image"
                val directory = File(path)
                if (!directory.exists()) {
                    directory.mkdirs()
                }
                takePictureFile = File(directory, "$name.png")

                val photoURI: Uri = FileProvider.getUriForFile(
                    application,
                    BuildConfig.APPLICATION_ID + ".provider",
                    takePictureFile!!,
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                _takePictureIntent.emit(takePictureIntent)
            } catch (e: Exception) {
                responseMessageData = ResponseMessageData(
                    success = false,
                    message = e.localizedMessage ?: "Unknown error",
                    code = "40"
                )
            } finally {
                responseMessageData?.let {
                    _takePictureResponse.emit(
                        ResponseMessage.fromRequest(
                            request = requestMessage,
                            data = it,
                        )
                    )
                }
            }
        }
        return null
    }

    suspend fun handleTakePictureResult(success: Boolean) {
        if (success) {
            takePictureRequest?.let {
                _takePictureResponse.emit(
                    ResponseMessage.fromRequest(
                        request = it,
                        data = ResponseMessageData(
                            success = true,
                            message = "Take picture success",
                            code = "40",
                            data = mapOf(
                                "path" to "image://img.m.pro${takePictureFile?.path}",
                            ),
                        )
                    )
                )
            }
        } else {
            takePictureRequest?.let {
                _takePictureResponse.emit(
                    ResponseMessage.fromRequest(
                        request = it,
                        data = ResponseMessageData(
                            success = false,
                            message = "Take picture fail",
                            code = "50",
                        )
                    )
                )
            }
        }
        takePictureRequest = null
        takePictureFile = null
    }
}