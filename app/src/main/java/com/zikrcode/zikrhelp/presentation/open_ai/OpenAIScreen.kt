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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.zikrhelp.R
import com.zikrcode.zikrhelp.presentation.open_ai.components.MessageContent
import com.zikrcode.zikrhelp.presentation.utils.composables.ImageContent
import com.zikrcode.zikrhelp.presentation.utils.composables.ResultContent
import com.zikrcode.zikrhelp.utils.Dimens
import com.zikrcode.zikrhelp.presentation.utils.composables.AppDropdownMenu
import com.zikrcode.zikrhelp.presentation.utils.composables.OpenAIModel
import com.zikrcode.zikrhelp.ui.theme.ZikrHelpTheme
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Composable
fun OpenAIScreen(
    openDrawer: () -> Unit,
    viewModel: OpenAIViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OpenAIContent(
        openDrawer = openDrawer,
        model = uiState.model,
        onActionModelSelect = viewModel::modelSelected,
        loading = uiState.isLoading,
        imageUri = uiState.imageUri,
        onImageSelect = viewModel::imageSelected,
        message = uiState.message,
        onMessageChange = viewModel::messageChanged,
        onSendMessage = viewModel::sendMessage,
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
fun OpenAIScreenPreview() {
    ZikrHelpTheme {
        OpenAIContent(
            openDrawer = { },
            model = OpenAIModel.GPT_4O,
            onActionModelSelect = { },
            loading = false,
            imageUri = null,
            onImageSelect = { },
            message = "",
            onMessageChange = { },
            onSendMessage = { },
            resultAvailable = false,
            result = null,
            onResultChange = { }
        )
    }
}

@Composable
private fun OpenAIContent(
    openDrawer: () -> Unit,
    model: OpenAIModel,
    onActionModelSelect: (OpenAIModel) -> Unit,
    loading: Boolean,
    imageUri: Uri?,
    onImageSelect: (Uri) -> Unit,
    message: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: (String?) -> Unit,
    resultAvailable: Boolean,
    result: String?,
    onResultChange: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            OpenAITopAppBar(
                openDrawer = openDrawer,
                actionModel = model,
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
                when (model) {
                    OpenAIModel.GPT_4O,
                    OpenAIModel.GPT_4O_MINI,
                    OpenAIModel.O1_PREVIEW,
                    OpenAIModel.O1_MINI -> {
                        MessageLayout(
                            message = message,
                            onMessageChange = onMessageChange,
                            onSendMessage = onSendMessage,
                            resultAvailable = resultAvailable,
                            result = result,
                            onResultChange = onResultChange
                        )
                    }

                    OpenAIModel.GPT_4O_VISION,
                    OpenAIModel.GPT_4O_MINI_VISION -> {
                        VisionLayout(
                            imageUri = imageUri,
                            onImageSelect = onImageSelect,
                            message = message,
                            onMessageChange = onMessageChange,
                            onSendMessage = onSendMessage,
                            resultAvailable = resultAvailable,
                            result = result,
                            onResultChange = onResultChange
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OpenAITopAppBar(
    openDrawer: () -> Unit,
    actionModel: OpenAIModel,
    onActionModelSelect: (OpenAIModel) -> Unit
) {
    val openAIModels = OpenAIModel.entries.toList()

    TopAppBar(
        title = {
            Text(text = stringResource(R.string.open_ai))
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
                models = openAIModels,
                selectedModel = actionModel
            ) {
                onActionModelSelect(it)
            }
            Spacer(Modifier.width(Dimens.SpacingSingleHalf))
        }
    )
}

@Composable
private fun MessageLayout(
    message: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: (String?) -> Unit,
    resultAvailable: Boolean,
    result: String?,
    onResultChange: (String) -> Unit
) {
    MessageContent(
        message = message,
        onMessageChange = onMessageChange
    ) {
        onSendMessage(null)
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

@Composable
private fun VisionLayout(
    imageUri: Uri?,
    onImageSelect: (Uri) -> Unit,
    message: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: (String?) -> Unit,
    resultAvailable: Boolean,
    result: String?,
    onResultChange: (String) -> Unit
) {
    val context = LocalContext.current

    ImageContent(
        modifier = Modifier.fillMaxWidth(),
        imageUri = imageUri,
        onImageSelect = onImageSelect
    )
    Spacer(Modifier.height(Dimens.SpacingSingle))
    MessageContent(
        message = message,
        onMessageChange = onMessageChange
    ) {
        val encodedImage = imageUri?.let { encodeImageToBase64(context, it) }
        onSendMessage(encodedImage)
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

private fun encodeImageToBase64(context: Context, imageUri: Uri): String? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        Base64.encodeToString(byteArray, Base64.DEFAULT)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}