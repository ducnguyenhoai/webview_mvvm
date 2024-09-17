package com.nhd.webviewmvvm.usecase

import android.app.Application
import android.content.Context
import com.nhd.webviewmvvm.FileUtils
import com.nhd.webviewmvvm.LocalStorageUtils
import com.nhd.webviewmvvm.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

class WebPackageUseCase @Inject constructor(private val application: Application) {

    private val tag = this::class.java.simpleName

    suspend fun checkExist(
        name: String,
    ): Boolean = withContext(Dispatchers.IO) {
        val directory = File("${application.applicationContext.filesDir}/Profile-0/$name")
        return@withContext directory.exists()
    }

    suspend fun update(name: String, url: String) = withContext(Dispatchers.IO) {
        val webPackage: JSONObject? = FileUtils.getJsonFromUrl(url)
        LogUtils.d(tag, "update $name - webPackage: $webPackage")
        if (webPackage == null) {
            return@withContext false
        }

        val version: String = webPackage.optString("version", "")
        val versionNumber: Int = FileUtils.getIntVersion(version)
        val currentVersion: Int = LocalStorageUtils.loadAppVersion(application.applicationContext, name)
        val pathStorage = "${application.applicationContext.filesDir}/Profile-0/$name"
        val isUpdate: Boolean =
            currentVersion == -1 || currentVersion < versionNumber || !File(pathStorage).exists()
        LogUtils.d(tag, "update $name - isUpdate: $isUpdate")
        if (!isUpdate) {
            return@withContext false
        }

        val urlZip: String = webPackage.optString("urlZip", "")
        if (urlZip.isBlank()) {
            LogUtils.e(tag, "update $name - urlZip is blank", null)
            return@withContext false
        }

        val downloadOutputPath = "${application.applicationContext.filesDir}/Profile-0/$name.zip"
        val downloadSuccess: Boolean = FileUtils.downloadFile(urlZip, downloadOutputPath, true) { downloadProgress ->
            LogUtils.d(tag, "update $name - download progress: $downloadProgress")
        }
        LogUtils.d(tag, "update $name - downloadSuccess: $downloadSuccess")
        if (!downloadSuccess) {
            return@withContext false
        }

        val unzipDirectoryPath = "${application.applicationContext.filesDir}/Profile-0/$name"
        val unzipSuccess: Boolean = FileUtils.unzipFile(downloadOutputPath, unzipDirectoryPath, true) { unzipProgress ->
            LogUtils.d(tag, "update $name - unzip progress: $unzipProgress")
        }
        LogUtils.d(tag, "update $name - unzipSuccess: $unzipSuccess")
        return@withContext unzipSuccess
    }

}