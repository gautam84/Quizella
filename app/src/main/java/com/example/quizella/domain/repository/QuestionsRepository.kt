package com.example.quizella.domain.repository

import com.example.quizella.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionsRepository {
    suspend fun getQuestions(): Flow<List<Question>>
    suspend fun clearQuestions()

}