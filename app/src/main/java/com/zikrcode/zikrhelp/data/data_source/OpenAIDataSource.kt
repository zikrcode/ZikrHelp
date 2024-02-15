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

import com.zikrcode.zikrhelp.BuildConfig
import com.zikrcode.zikrhelp.data.model.open_ai_model.OpenAIRequest
import com.zikrcode.zikrhelp.data.model.open_ai_model.OpenAIResult
import com.zikrcode.zikrhelp.utils.AppConstants.OPEN_AI_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class OpenAIDataSource @Inject constructor(
    private val client: OkHttpClient
) {

    suspend fun completeMessage(
        openAIRequest: OpenAIRequest
    ): OpenAIResult? = withContext(Dispatchers.IO) {
        val jsonString = Json.encodeToString(openAIRequest)
        val body = jsonString.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(OPEN_AI_URL)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${BuildConfig.OPEN_AI_API_KEY}")
            .post(body)
            .build()

        try {
            val response = client.newCall(request).execute()
            val jsonDecoder = Json {
                ignoreUnknownKeys = true
            }
            response.body?.string()?.let {
                jsonDecoder.decodeFromString<OpenAIResult>(it)
            }
        } catch (e: Exception) {
            null
        }
    }
}