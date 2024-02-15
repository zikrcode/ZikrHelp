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

package com.zikrcode.zikrhelp.data.repository

import com.zikrcode.zikrhelp.data.data_source.OpenAIDataSource
import com.zikrcode.zikrhelp.data.model.open_ai_model.OpenAIRequest
import javax.inject.Inject

class OpenAIRepository @Inject constructor(
    private val dataSource: OpenAIDataSource
) {

    suspend fun completeMessage(openAIRequest: OpenAIRequest) =
        dataSource.completeMessage(openAIRequest)
}