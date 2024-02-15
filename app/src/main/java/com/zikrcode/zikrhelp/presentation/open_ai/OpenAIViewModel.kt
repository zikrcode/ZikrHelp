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
import com.zikrcode.zikrhelp.data.model.open_ai_model.DefaultMessage
import com.zikrcode.zikrhelp.data.model.open_ai_model.Model
import com.zikrcode.zikrhelp.data.model.open_ai_model.OpenAIRequest
import com.zikrcode.zikrhelp.data.model.open_ai_model.Role
import com.zikrcode.zikrhelp.data.model.open_ai_model.VisionContent
import com.zikrcode.zikrhelp.data.model.open_ai_model.VisionContentType
import com.zikrcode.zikrhelp.data.model.open_ai_model.VisionMessage
import com.zikrcode.zikrhelp.data.repository.OpenAIRepository
import com.zikrcode.zikrhelp.presentation.utils.composables.AppModel
import com.zikrcode.zikrhelp.presentation.utils.composables.OpenAIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OpenAIUiState(
    val openAIModel: AppModel = OpenAIModel.GPT_4,
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

    fun modelSelected(model: AppModel) {
        _uiState.update {
            it.copy(openAIModel = model)
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
            when (_uiState.value.openAIModel) {
                OpenAIModel.GPT_4 -> {
                    val messages = listOf(
                        DefaultMessage(
                            role = Role.USER,
                            content = _uiState.value.message
                        )
                    )
                    val openAIRequest = OpenAIRequest(
                        model = Model.GPT_4,
                        messages = messages,
                        maxTokens = 4096
                    )
                    val openAIResult = repository.completeMessage(openAIRequest)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isResultAvailable = true,
                            result = openAIResult?.choices?.firstOrNull()?.message?.content
                        )
                    }
                }
                OpenAIModel.GPT_4_VISION_PREVIEW -> {
                    val content = listOf(
                        VisionContent(type = VisionContentType.TEXT, text = _uiState.value.message),
                        VisionContent(type = VisionContentType.IMAGE_URL, imageUrl = "data:image/jpeg;base64,$encodedImage")
                    )
                    val messages = listOf(
                        VisionMessage(
                            role = Role.USER,
                            content = content
                        )
                    )
                    val openAIRequest = OpenAIRequest(
                        model = Model.GPT_4_VISION_PREVIEW,
                        messages = messages,
                        maxTokens = 4096
                    )
                    val openAIResult = repository.completeMessage(openAIRequest)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isResultAvailable = true,
                            result = openAIResult?.choices?.firstOrNull()?.message?.content
                        )
                    }
                }
                else -> { }
            }
        }
    }

    fun resultChanged(value: String) {
        _uiState.update {
            it.copy(result = value)
        }
    }
}