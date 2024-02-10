package com.zikrcode.zikrhelp.data.model.open_ai_model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResult(
    val model: String,
    val choices: List<OpenAIChoice>,
    val usage: OpenAIUsage
)

@Serializable
data class OpenAIChoice(
    val message: ChoiceMessage,
    @SerialName("finish_reason") val finishReason: String
)

@Serializable
data class ChoiceMessage(
    val role: String,
    val content: String
)

@Serializable
data class OpenAIUsage(
    @SerialName("prompt_tokens") val promptTokens: Int,
    @SerialName("completion_tokens") val completionTokens: Int,
    @SerialName("total_tokens") val totalTokens: Int
)