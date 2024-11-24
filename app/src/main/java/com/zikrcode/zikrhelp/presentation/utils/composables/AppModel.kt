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

sealed interface AppModel {
    val value: String
}

enum class MLKitModel(override val value: String) : AppModel {
    TEXT_RECOGNITION("Text recognition v2"),
    BARCODE_SCANNING("Barcode scanning")
}

enum class OpenAIModel(override val value: String) : AppModel {
    GPT_4O("GPT-4o"),
    GPT_4O_VISION("GPT-4o (Vision)"),
    GPT_4O_MINI("GPT-4o mini"),
    GPT_4O_MINI_VISION("GPT-4o mini (Vision)"),

    O1_PREVIEW("o1-preview"),
    O1_MINI("o1-mini"),
}