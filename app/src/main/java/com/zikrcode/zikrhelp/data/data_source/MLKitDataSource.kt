package com.zikrcode.zikrhelp.data.data_source

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.zikrcode.zikrhelp.presentation.utils.AppConstants.LINE_SEPARATOR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MLKitDataSource @Inject constructor() {

    suspend fun detectText(inputImage: InputImage): String? = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    continuation.resume(mapVisionText(visionText))
                }
                .addOnFailureListener { _ ->
                    continuation.resume(null)
                }
        }
    }

    private fun mapVisionText(visionText: Text): String {
        val resultText = StringBuilder()
        for (block in visionText.textBlocks) {
            for (line in block.lines) {
                val lineText = line.text
                resultText.append(lineText)
                resultText.append(LINE_SEPARATOR)
            }
        }
        return resultText.toString()
    }
}