/**
 *
 *	MIT License
 *
 *	Copyright (c) 2023 Gautam Hazarika
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all
 *	copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *	SOFTWARE.
 *
 **/

package com.example.quizella.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.quizella.data.data_source.local.QuestionsCacheDB
import com.example.quizella.data.data_source.remote.TriviaService
import com.example.quizella.data.repository.QuestionsRepositoryImpl
import com.example.quizella.data.repository.ScoreRepositoryImpl
import com.example.quizella.domain.repository.QuestionsRepository
import com.example.quizella.domain.repository.ScoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesQuestionsCacheDB(app: Application): QuestionsCacheDB {
        return Room.databaseBuilder(
            app,
            QuestionsCacheDB::class.java,
            QuestionsCacheDB.DATABASE_NAME
        ).build()

    }

    @Provides
    @Singleton
    fun providesScoreRepository(
        @ApplicationContext context: Context
    ): ScoreRepository = ScoreRepositoryImpl(context = context)

    @Provides
    @Singleton
    fun providesHttpClient() = HttpClient(Android) {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("NetworkMessage", "log: $message")
                }
            }
            level = LogLevel.ALL
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(HttpTimeout) {
            socketTimeoutMillis = 30_000
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 30_000
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }

    @Provides
    @Singleton
    fun providesTriviaService(client: HttpClient) = TriviaService(client)


    @Provides
    @Singleton
    fun providesQuestionsRepository(
        db: QuestionsCacheDB,
        triviaService: TriviaService
    ): QuestionsRepository =
        QuestionsRepositoryImpl(db.questionCacheDao, triviaService)

}