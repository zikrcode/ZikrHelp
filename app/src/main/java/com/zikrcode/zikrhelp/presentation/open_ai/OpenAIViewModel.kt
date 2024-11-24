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

package com.zikrcode.zikrhelp.presentation.open_ai

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zikrcode.zikrhelp.data.data_source.ApiResponse
import com.zikrcode.zikrhelp.data.model.open_ai_model.ChatCompletionRequest
import com.zikrcode.zikrhelp.data.model.open_ai_model.DefaultMessage
import com.zikrcode.zikrhelp.data.model.open_ai_model.ImageUrl
import com.zikrcode.zikrhelp.data.model.open_ai_model.Model
import com.zikrcode.zikrhelp.data.model.open_ai_model.Role
import com.zikrcode.zikrhelp.data.model.open_ai_model.VisionContent
import com.zikrcode.zikrhelp.data.model.open_ai_model.VisionContentType
import com.zikrcode.zikrhelp.data.model.open_ai_model.VisionMessage
import com.zikrcode.zikrhelp.data.repository.OpenAIRepository
import com.zikrcode.zikrhelp.presentation.utils.composables.OpenAIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OpenAIUiState(
    val model: OpenAIModel = OpenAIModel.GPT_4O,
    val isLoading: Boolean = false,
    val imageUri: Uri? = null,
    val message: String = "",
    val isResultAvailable: Boolean = false,
    val result: String? = null
)

@HiltViewModel
class OpenAIViewModel @Inject constructor(
    private val repository: OpenAIRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OpenAIUiState())
    val uiState = _uiState.asStateFlow()

    fun modelSelected(model: OpenAIModel) {
        _uiState.update {
            it.copy(model = model)
        }
    }

    fun imageSelected(uri: Uri) {
        if (uri == _uiState.value.imageUri) return
        _uiState.update {
            it.copy(
                imageUri = uri,
                isResultAvailable = false,
                result = null
            )
        }
    }

    fun messageChanged(value: String) {
        _uiState.update {
            it.copy(message = value)
        }
    }

    fun sendMessage(encodedImage: String?) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            when (_uiState.value.model) {
                OpenAIModel.GPT_4O -> {
                    chatCompletion(model = Model.GPT_4O)
                }
                OpenAIModel.GPT_4O_VISION -> {
                    chatCompletionVision(encodedImage = encodedImage, model = Model.GPT_4O)
                }
                OpenAIModel.GPT_4O_MINI -> {
                    chatCompletion(model = Model.GPT_4O_MINI)
                }
                OpenAIModel.GPT_4O_MINI_VISION -> {
                    chatCompletionVision(encodedImage = encodedImage, model = Model.GPT_4O_MINI)
                }
                OpenAIModel.O1_PREVIEW -> {
                    chatCompletion(model = Model.O1_PREVIEW)
                }
                OpenAIModel.O1_MINI -> {
                    chatCompletion(model = Model.O1_MINI)
                }
            }
        }
    }

    private suspend fun chatCompletion(model: Model) {
        val messages = listOf(
            DefaultMessage(
                role = Role.USER,
                content = _uiState.value.message
            )
        )
        val request = ChatCompletionRequest(
            model = model,
            messages = messages
        )
        when (val result = repository.completeChat(request)) {
            is ApiResponse.Error -> {
                TODO()
            }
            is ApiResponse.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isResultAvailable = true,
                        result = result.data.choices.first().message.content
                    )
                }
            }
        }
    }

    private suspend fun chatCompletionVision(
        encodedImage: String?,
        model: Model
    ) {
        val content = listOf(
            VisionContent(
                type = VisionContentType.TEXT,
                text = _uiState.value.message
            ),
            VisionContent(
                type = VisionContentType.IMAGE_URL,
                imageUrl = ImageUrl(url = "data:image/jpeg;base64,$encodedImage")
            )
        )
        val messages = listOf(
            VisionMessage(
                role = Role.USER,
                content = content
            )
        )
        val request = ChatCompletionRequest(
            model = model,
            messages = messages
        )
        when (val result = repository.completeChat(request)) {
            is ApiResponse.Error -> {
                TODO()
            }
            is ApiResponse.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isResultAvailable = true,
                        result = result.data.choices.first().message.content
                    )
                }
            }
        }
    }

    fun resultChanged(value: String) {
        _uiState.update {
            it.copy(result = value)
        }
    }
}