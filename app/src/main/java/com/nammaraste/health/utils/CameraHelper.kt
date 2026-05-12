package com.nammaraste.health.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object CameraHelper {
    fun createImageUri(context: Context): Uri {
        val imageFile = File(context.cacheDir, "damage_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
    }
}
