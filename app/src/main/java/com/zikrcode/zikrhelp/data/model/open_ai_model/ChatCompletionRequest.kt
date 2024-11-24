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

package com.zikrcode.zikrhelp.data.model.open_ai_model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

//Request
@Serializable
data class ChatCompletionRequest(
    val model: Model,
    val messages: List<Message>,
)

@Serializable
enum class Model {
    @SerialName("gpt-4o") GPT_4O,
    @SerialName("gpt-4o-mini") GPT_4O_MINI,
    @SerialName("o1-preview") O1_PREVIEW,
    @SerialName("o1-mini") O1_MINI
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
    @SerialName("image_url") val imageUrl: ImageUrl? = null
)

@Serializable
data class ImageUrl(val url: String)

@Serializable
enum class VisionContentType {
    @SerialName("text") TEXT,
    @SerialName("image_url") IMAGE_URL
}