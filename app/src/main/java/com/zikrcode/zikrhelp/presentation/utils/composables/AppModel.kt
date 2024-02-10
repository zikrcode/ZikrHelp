package com.zikrcode.zikrhelp.presentation.utils.composables

sealed interface AppModel {
    val value: String
}

enum class MLKitModel(override val value: String) : AppModel {
    TEXT_RECOGNITION("Text recognition v2"),
    BARCODE_SCANNING("Barcode scanning")
}

enum class OpenAIModel(override val value: String) : AppModel {
    GPT_4("GPT-4"),
    GPT_4_VISION_PREVIEW("GPT-4 Vision Preview")
}