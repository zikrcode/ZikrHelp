package com.zikrcode.zikrhelp.presentation.utils

sealed interface AppModel {
    val value: String
}

enum class MLKitModel(override val value: String) : AppModel {
    TEXT_RECOGNITION("Text recognition v2"),
    BARCODE_SCANNING("Barcode scanning")
}

enum class OpenAIModel(override val value: String) : AppModel {
    GPT_4_VISION_PREVIEW("GPT-4 Vision Preview"),
    GPT_4_TEXT_GENERATION("GPT-4 Text Generation")
}