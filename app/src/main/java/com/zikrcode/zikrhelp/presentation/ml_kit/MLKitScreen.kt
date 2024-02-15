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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.mlkit.vision.common.InputImage
import com.zikrcode.zikrhelp.R
import com.zikrcode.zikrhelp.presentation.utils.composables.ImageContent
import com.zikrcode.zikrhelp.presentation.utils.composables.ResultContent
import com.zikrcode.zikrhelp.presentation.utils.composables.AppDropdownMenu
import com.zikrcode.zikrhelp.presentation.utils.composables.AppModel
import com.zikrcode.zikrhelp.utils.Dimens
import com.zikrcode.zikrhelp.presentation.utils.composables.MLKitModel
import com.zikrcode.zikrhelp.ui.theme.ZikrHelpTheme

@Composable
fun MLKitScreen(
    openDrawer: () -> Unit,
    viewModel: MLKitViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MLKitContent(
        openDrawer = openDrawer,
        actionModel = uiState.mlKitModel,
        onActionModelSelect = viewModel::modelSelected,
        loading = uiState.isLoading,
        imageUri = uiState.imageUri,
        onImageSelect = viewModel::imageSelected,
        onDetectText = viewModel::detectTextFromInputImage,
        resultAvailable = uiState.isResultAvailable,
        result = uiState.result,
        onResultChange = viewModel::resultChanged
    )
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    device = Devices.PHONE
)
@Composable
fun MlKitScreenPreview() {
    ZikrHelpTheme {
        MLKitContent(
            openDrawer = { },
            actionModel = MLKitModel.TEXT_RECOGNITION,
            onActionModelSelect = { },
            loading = false,
            imageUri = null,
            onImageSelect = { },
            onDetectText = { },
            resultAvailable = false,
            result = "",
            onResultChange = { },
        )
    }
}

@Composable
private fun MLKitContent(
    openDrawer: () -> Unit,
    actionModel: AppModel,
    onActionModelSelect: (AppModel) -> Unit,
    loading: Boolean,
    imageUri: Uri?,
    onImageSelect: (Uri) -> Unit,
    onDetectText: (InputImage) -> Unit,
    resultAvailable: Boolean,
    result: String?,
    onResultChange: (String) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            MLKitTopAppBar(
                openDrawer = openDrawer,
                actionModel = actionModel,
                onActionModelSelect = onActionModelSelect
            )
        },
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(Dimens.SpacingQuadruple),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(Dimens.SpacingDouble)
                    .verticalScroll(rememberScrollState())
            ) {
                ImageContent(
                    modifier = Modifier.fillMaxWidth(),
                    imageUri = imageUri,
                    onImageSelect = onImageSelect
                )
                Spacer(Modifier.height(Dimens.SpacingSingleHalf))
                imageUri?.let { uri ->
                    DetectTextButton {
                        onDetectText(
                            InputImage.fromFilePath(context, uri)
                        )
                    }
                }
                if (resultAvailable) {
                    ResultContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.SpacingSingle)
                            .windowInsetsPadding(WindowInsets.navigationBars),
                        result = result,
                        onResultChange = onResultChange
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MLKitTopAppBar(
    openDrawer: () -> Unit,
    actionModel: AppModel,
    onActionModelSelect: (AppModel) -> Unit
) {
    val mlKitModels = MLKitModel.entries.toList()

    TopAppBar(
        title = {
            Text(text = stringResource(R.string.ml_kit))
        },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = stringResource(R.string.open_drawer)
                )
            }
        },
        actions = {
            AppDropdownMenu(
                models = mlKitModels,
                selectedModel = actionModel
            ) {
                onActionModelSelect(it)
            }
            Spacer(Modifier.width(Dimens.SpacingSingleHalf))
        }
    )
}

@Composable
private fun DetectTextButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Icon(
            painter = painterResource(R.drawable.ic_detect),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.detect_text),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}