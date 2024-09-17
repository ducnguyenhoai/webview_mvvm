package com.nhd.webviewmvvm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipFile

object FileUtils {

    private val tag = this::class.java.simpleName

    suspend fun downloadFile(
        url: String,
        outputPath: String,
        isUpdate: Boolean = false,
        onProgress: (Double) -> Unit = {}
    ): Boolean = withContext(Dispatchers.IO) {
        val file = File(outputPath)

        // Check if file already exists and handle based on the `isUpdate` flag
        if (file.exists()) {
            if (isUpdate) {
                file.delete()
            } else {
                LogUtils.d(tag, "File already exists at: $outputPath")
                return@withContext true
            }
        }

        // Ensure the parent directory exists
        val parentDir = file.parentFile ?: return@withContext false.also {
            LogUtils.d(tag, "Parent directory is null")
        }
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            LogUtils.d(tag, "Failed to create parent directory: ${parentDir.path}")
            return@withContext false
        }

        // Create a new file
        if (!file.createNewFile()) {
            LogUtils.d(tag, "Failed to create file: $outputPath")
            return@withContext false
        }

        try {
            // Build the request
            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            // Execute the network request
            val response = DownloadHttpClient.okHttp.newCall(request).execute()

            // Check for a successful response
            if (response.isSuccessful) {
                val responseBody = response.body
                val contentLength = responseBody.contentLength()

                // Open file output stream and write the response body
                FileOutputStream(file).use { outputStream ->
                    responseBody.byteStream().use { inputStream ->
                        val buffer = ByteArray(8 * 1024) // 8 KB buffer
                        var bytesRead: Int
                        var totalBytesRead: Long = 0

                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                            totalBytesRead += bytesRead

                            // Ensure no division by zero when content length is unknown
                            if (contentLength > 0) {
                                onProgress((totalBytesRead * 100.0) / contentLength)
                            } else {
                                onProgress(0.0) // No content length, progress cannot be calculated
                            }
                        }
                        outputStream.flush()
                    }
                }
                LogUtils.d(tag, "File $url downloaded successfully: $outputPath")
                return@withContext true
            } else {
                LogUtils.d(
                    tag,
                    "Failed to download file $url. Response code: ${response.code}"
                )
                return@withContext false
            }
        } catch (e: IOException) {
            LogUtils.e(tag, "IOException occurred while downloading file $url", e)
            return@withContext false
        } catch (e: Exception) {
            LogUtils.e(tag, "Unexpected error occurred while downloading file $url", e)
            return@withContext false
        }
    }

    fun unzipFile(
        path: String,
        outputPath: String,
        isUpdate: Boolean = false,
        updateProgress: ((Float) -> Unit)? = null
    ): Boolean {
        val directory = File(outputPath)

        // Check if the directory already exists
        if (directory.exists() && !isUpdate) {
            updateProgress?.invoke(100f)
            LogUtils.d(tag, "Directory already exists: $outputPath")
            return true
        } else if (!directory.exists()) {
            if (!directory.mkdirs()) {
                LogUtils.e(tag, "Failed to create directory: $outputPath", null)
                return false
            }
        }

        val zipFile = File(path)
        if (!zipFile.exists()) {
            LogUtils.d(tag, "Zip file does not exist: $path")
            return false
        }

        try {
            ZipFile(zipFile).use { zip ->
                val entries =
                    zip.entries().asSequence().filter { !it.name.startsWith("__MACOSX") }.toList()
                val total = entries.size
                var totalUnzipped = 0

                for (entry in entries) {
                    val outputFile = File(outputPath, entry.name)

                    // Ensure directory structure is created
                    if (entry.isDirectory) {
                        if (!outputFile.exists()) {
                            outputFile.mkdirs()
                        }
                    } else {
                        val outputFileParent = outputFile.parentFile
                        if (outputFileParent != null && !outputFileParent.exists()) {
                            outputFileParent.mkdirs()
                        }

                        // Create the new file if it doesn't exist
                        if (!outputFile.exists()) {
                            outputFile.createNewFile()
                        }

                        // Write the file content
                        zip.getInputStream(entry).use { input ->
                            outputFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }

                    // Update progress
                    totalUnzipped++
                    updateProgress?.invoke((totalUnzipped * 100f) / total)
                }
            }

            // Optionally delete the zip file after extraction
            zipFile.delete()

            LogUtils.d(tag, "Unzip $path completed successfully: $outputPath")
            return true
        } catch (e: IOException) {
            LogUtils.e(tag, "IOException during unzip $path", e)
            return false
        } catch (e: Exception) {
            LogUtils.e(tag, "Unexpected error during unzip $path", e)
            return false
        }
    }

    suspend fun getJsonFromUrl(url: String): JSONObject? = withContext(Dispatchers.IO) {
        if (url.isBlank()) {
            LogUtils.e(tag, "URL is blank.", null)
            return@withContext null
        }

        try {
            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            val response = HttpClient.okHttp.newCall(request).execute()

            if (response.isSuccessful) {
                response.body.string().let { result ->
                    return@withContext try {
                        JSONObject(result)
                    } catch (e: JSONException) {
                        LogUtils.e(tag, "Failed to parse JSON", e)
                        null
                    }
                }
            } else {
                LogUtils.e(
                    tag,
                    "Failed to fetch data. Status code: ${response.code} for URL: $url",
                    null
                )
                return@withContext null
            }

        } catch (e: IOException) {
            LogUtils.e(tag, "IOException occurred while downloading json $url", e)
            return@withContext null
        } catch (e: Exception) {
            LogUtils.e(tag, "Unexpected error occurred while downloading json $url", e)
            return@withContext null
        }
    }

    fun getIntVersion(versionStr: String): Int {
        try {
            val versionSplit: List<String> = versionStr.split(".")
            if (versionSplit.size != 3) {
                return -1
            }
            val first = versionSplit[0].toInt()
            val mid = versionSplit[1].toInt()
            val last = versionSplit[2].toInt()
            return first * 1000000 + mid * 10000 + last
        } catch (e: Exception) {
            return -1
        }
    }

}