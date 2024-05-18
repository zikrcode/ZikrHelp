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

import com.zikrcode.zikrhelp.data.model.open_ai_model.ChatCompletionRequest
import com.zikrcode.zikrhelp.data.model.open_ai_model.ChatCompletionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OpenAIDataSource @Inject constructor(
    private val service: OpenAIService
) {

    suspend fun completeChat(
        chatCompletionRequest: ChatCompletionRequest
    ): ApiResponse<ChatCompletionResponse> = withContext(Dispatchers.IO) {
        try {
            val response = service.completeChat(chatCompletionRequest)
            if (response.isSuccessful) {
                response.body()?.let { chatCompletionRequest ->
                    ApiResponse.Success(chatCompletionRequest)
                } ?: ApiResponse.Error(Exception(response.message()))
            } else {
                ApiResponse.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }
}