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

package com.zikrcode.zikrhelp.presentation.ml_kit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.zikrcode.zikrhelp.data.repository.MLKitRepository
import com.zikrcode.zikrhelp.presentation.utils.composables.AppModel
import com.zikrcode.zikrhelp.presentation.utils.composables.MLKitModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MLKitUiState(
    val mlKitModel: AppModel = MLKitModel.TEXT_RECOGNITION,
    val isLoading: Boolean = false,
    val imageUri: Uri? = null,
    val isResultAvailable: Boolean = false,
    val result: String? = null
)

@HiltViewModel
class MLKitViewModel @Inject constructor(
    private val repository: MLKitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MLKitUiState())
    val uiState = _uiState.asStateFlow()

    fun modelSelected(model: AppModel) {
        _uiState.update {
            it.copy(mlKitModel = model)
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

    fun detectTextFromInputImage(inputImage: InputImage) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val result = repository.detectText(inputImage)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isResultAvailable = true,
                    result = result
                )
            }
        }
    }

    fun resultChanged(value: String) {
        _uiState.update {
            it.copy(result = value)
        }
    }
}