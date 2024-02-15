/*
 * Copyright 2024 Zokirjon Mamadjonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zikrcode.zikrhelp.data.data_source

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.zikrcode.zikrhelp.utils.AppConstants.LINE_SEPARATOR
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