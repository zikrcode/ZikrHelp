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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zikrcode.zikrhelp.R
import com.zikrcode.zikrhelp.utils.Dimens
import com.zikrcode.zikrhelp.ui.theme.ZikrHelpTheme

@Composable
fun AppDropdownMenu(
    models: List<AppModel>,
    selectedModel: AppModel,
    onModelSelect: (AppModel) -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .width(220.dp)
            .clip(CircleShape)
            .clickable { expanded = !expanded }
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(Dimens.SpacingSingleHalf),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = selectedModel.value,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Box {
            Icon(
                painter = painterResource(
                    if (expanded) R.drawable.ic_expand_less else R.drawable.ic_expand_more
                ),
                contentDescription = null
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = !expanded }
            ) {
                models.forEach { model ->
                    DropdownMenuItem(
                        text = { Text(text = model.value) },
                        onClick = {
                            expanded = !expanded
                            onModelSelect(model)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AddCounterFormPreview() {
    ZikrHelpTheme {
        AppDropdownMenu(
            models = listOf(),
            selectedModel = MLKitModel.TEXT_RECOGNITION,
            onModelSelect =  { }
        )
    }
}