package com.jainhardik120.rchat.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.jainhardik120.rchat.R
import com.jainhardik120.rchat.data.KeyValueStorage
import com.jainhardik120.rchat.data.remote.ChatSocketService
import com.jainhardik120.rchat.data.remote.ChatSocketServiceImpl
import com.jainhardik120.rchat.data.remote.RChatApi
import com.jainhardik120.rchat.data.remote.RChatApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            expectSuccess = true
            install(Logging) {
                level = LogLevel.ALL
            }
            install(WebSockets)
            install(ContentNegotiation) {
                json(
                    kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    @Provides
    @Singleton
    fun provideRChatApi(client: HttpClient, keyValueStorage: KeyValueStorage): RChatApi {
        return RChatApiImpl(client, keyValueStorage)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): KeyValueStorage {
        return KeyValueStorage(
            context.getSharedPreferences(
                context.resources.getString(R.string.app_name),
                Context.MODE_PRIVATE
            )
        )
    }

    @Provides
    @Singleton
    fun provideChatSocketService(client: HttpClient, keyValueStorage: KeyValueStorage): ChatSocketService {
        return ChatSocketServiceImpl(client, keyValueStorage)
    }
}