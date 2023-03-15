package com.example.quizella.data.data_source.remote

import android.util.Log
import com.example.quizella.common.Constants
import com.example.quizella.data.data_source.remote.dto.QuestionResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*


class TriviaService(
    private val client: HttpClient
) {
    suspend fun getQuestions(): List<QuestionResponse> {
        return try {
            client.get { url(Constants.base_url) }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            Log.d("Error", e.response.status.description)
            emptyList()
        } catch(e: ClientRequestException) {
            // 4xx - responses
            Log.d("Error", e.response.status.description)

            emptyList()
        } catch(e: ServerResponseException) {
            // 5xx - responses
            Log.d("Error", e.response.status.description)
            emptyList()
        } catch(e: Exception) {
            println("Error: ${e.message}")
            emptyList()
        }
    }


}