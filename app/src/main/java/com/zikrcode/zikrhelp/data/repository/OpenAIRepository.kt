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