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

package com.zikrcode.zikrhelp.presentation.utils.composables

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.zikrcode.zikrhelp.R
import com.zikrcode.zikrhelp.utils.Dimens
import com.zikrcode.zikrhelp.provider.MainFileProvider
import com.zikrcode.zikrhelp.ui.theme.ZikrHelpTheme

@Composable
fun ImageContent(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    onImageSelect: (Uri) -> Unit
) {
    val context = LocalContext.current
    val newImageUri = MainFileProvider.createImageUri(context)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success -> if (success) onImageSelect(newImageUri) }
    )
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let { onImageSelect(it) } }
    )

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    cameraLauncher.launch(newImageUri)
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_take_image),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.take_image),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.width(Dimens.SpacingDouble))
            Button(
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_select_image),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.select_image),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(Modifier.height(Dimens.SpacingDouble))
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.CornerRadiusHalf))
                .border(
                    border = BorderStroke(Dimens.BorderSmall, Color.Gray),
                    shape = RoundedCornerShape(Dimens.CornerRadiusHalf)
                ),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Preview
@Composable
fun ImageContentPreview() {
    ZikrHelpTheme {
        ImageContent(
            imageUri = null,
            onImageSelect = { }
        )
    }
}