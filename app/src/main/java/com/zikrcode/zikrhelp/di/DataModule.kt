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

package com.zikrcode.zikrhelp.di

import com.zikrcode.zikrhelp.BuildConfig
import com.zikrcode.zikrhelp.data.data_source.OpenAIService
import com.zikrcode.zikrhelp.utils.AppConstants.CONTENT_TYPE
import com.zikrcode.zikrhelp.utils.AppConstants.MAX_TIMEOUT
import com.zikrcode.zikrhelp.utils.AppConstants.OPEN_AI_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val request = originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer ${BuildConfig.OPEN_AI_API_KEY}")
                .build()


            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(MAX_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(MAX_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(MAX_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val mediaType = CONTENT_TYPE.toMediaType()

        return Retrofit.Builder()
            .baseUrl(OPEN_AI_BASE_URL)
            .addConverterFactory(json.asConverterFactory(mediaType))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): OpenAIService {
        return retrofit.create(OpenAIService::class.java)
    }
}