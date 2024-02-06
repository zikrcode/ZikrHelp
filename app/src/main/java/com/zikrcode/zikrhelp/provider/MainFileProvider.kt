package com.zikrcode.zikrhelp.provider

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.zikrcode.zikrhelp.R
import java.io.File

class MainFileProvider : FileProvider(R.xml.file_paths) {

    companion object {

        fun createImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile("image_x_", ".jpg", directory)
            val authority = context.packageName + ".fileprovider"

            return getUriForFile(context, authority, file)
        }
    }
}