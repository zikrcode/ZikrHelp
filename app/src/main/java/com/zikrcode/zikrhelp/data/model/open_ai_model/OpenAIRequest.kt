package com.zikrcode.zikrhelp.data.model.open_ai_model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

//Request
@Serializable
data class OpenAIRequest(
    val model: Model,
    val messages: List<Message>,
    @SerialName("max_tokens") val maxTokens: Int
)

@Serializable
enum class Model {
    @SerialName("gpt-4") GPT_4,
    @SerialName("gpt-4-vision-preview") GPT_4_VISION_PREVIEW
}

//Message
@Serializable(with = MessageSerializer::class)
sealed interface Message {
    val role: Role
}

@Serializable
enum class Role {
    @SerialName("user") USER
}

@Serializable
data class DefaultMessage(
    override val role: Role,
    val content: String
) : Message

@Serializable
data class VisionMessage(
    override val role: Role,
    val content: List<VisionContent>
) : Message

object MessageSerializer : JsonContentPolymorphicSerializer<Message>(Message::class) {

    override fun selectDeserializer(element: JsonElement) = when {
        "content" in element.jsonObject &&
                element.jsonObject["content"]!!.jsonArray.isNotEmpty() -> VisionMessage.serializer()
        else -> DefaultMessage.serializer()
    }
}

//Content
@Serializable
data class VisionContent(
    val type: VisionContentType,
    val text: String? = null,
    @SerialName("image_url") var imageUrl: String? = null
)

@Serializable
enum class VisionContentType {
    @SerialName("text") TEXT,
    @SerialName("image_url") IMAGE_URL
}