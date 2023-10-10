package com.zexceed.restaurant.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi

object Constants {

    const val TAG = "Response::::::"

    const val API_BASE_URL = "http://192.168.100.47:5000/Api/"

    @RequiresApi(Build.VERSION_CODES.Q)
    fun exportDataToExternalFile(context: Context, filename: String, data: ByteArray, mimeType: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val resolver = context.contentResolver
        val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            val outputStream = resolver.openOutputStream(uri)
            outputStream?.write(data)
        }
    }
}