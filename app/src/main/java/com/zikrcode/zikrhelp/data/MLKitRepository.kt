package com.zikrcode.zikrhelp.data

import com.google.mlkit.vision.common.InputImage
import com.zikrcode.zikrhelp.data.data_source.MLKitDataSource
import javax.inject.Inject

class MLKitRepository @Inject constructor(
    private val dataSource: MLKitDataSource
) {

    suspend fun detectText(inputImage: InputImage) = dataSource.detectText(inputImage)
}