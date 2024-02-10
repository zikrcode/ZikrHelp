package com.zikrcode.zikrhelp.di

import com.zikrcode.zikrhelp.utils.AppConstants.MAX_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(MAX_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(MAX_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(MAX_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
}